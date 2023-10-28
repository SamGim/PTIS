package com.thewayhome.ptis.batch.job.b0002;

import com.thewayhome.ptis.batch.job.base.*;
import com.thewayhome.ptis.batch.job.util.BatchJobStatusNotificationListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class B0002JobConfig {
    @Bean
    @Qualifier("B0002Job")
    public Job B0002Job(
            JobRepository jobRepository,
            @Qualifier("B0002ValidateInputStep") Step validateInput,
            @Qualifier("B0002InitValueStep") Step initValue,
            @Qualifier("B0002DoMainLogicStep") Step doMainLogic,
            @Qualifier("B0002FinalizeJobStep") Step finalizeJob,
            @Qualifier("B0002FailureHandlingStep") Step failureHandling,
            BatchJobStatusNotificationListener batchJobStatusNotificationListener
    ) {
        return new JobBuilder("B0002", jobRepository)
                .start(validateInput).on("FAILED").to(failureHandling)
                .from(validateInput).on("*").to(initValue)
                .from(initValue).on("FAILED").to(failureHandling)
                .from(initValue).on("*").to(doMainLogic)
                .from(doMainLogic).on("FAILED").to(failureHandling)
                .from(doMainLogic).on("*").to(finalizeJob)
                .end()
                .listener(batchJobStatusNotificationListener)
                .build();
    }

    @Bean
    @Qualifier("B0002ValidateInputStep")
    public Step B0002ValidateInputStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0002ValidateInputTasklet") AbstractValidateInputTasklet validateInputTasklet
    ) {
        return new StepBuilder("B0002ValidateInputStep", jobRepository)
                .tasklet(validateInputTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0002InitValueStep")
    public Step B0002InitValueStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0002InitValueTasklet") AbstractInitValueTasklet initValueTasklet
    ) {
        return new StepBuilder("B0002InitValueStep", jobRepository)
                .tasklet(initValueTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0002DoMainLogicStep")
    public Step B0002DoMainLogicStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0002DoMainLogicItemReader") ItemReader<B0002DoMainLogicItemInput> doMainLogicItemReader,
            @Qualifier("B0002DoMainLogicItemProcessor") ItemProcessor<B0002DoMainLogicItemInput, B0002DoMainLogicItemOutput> doMainLogicItemProcessor,
            @Qualifier("B0002DoMainLogicItemWriter") ItemWriter<B0002DoMainLogicItemOutput> doMainLogicItemWriter,
            @Qualifier("B0002DoMainLogicChunkListener") ChunkListener doMainLogicChunkListener,
            B0002DoMainLogicRetryTemplate retryTemplate
    ) {
        return new StepBuilder("B0002DoMainLogicStep", jobRepository)
                .<B0002DoMainLogicItemInput, B0002DoMainLogicItemOutput> chunk(10, transactionManager)
                .reader(doMainLogicItemReader)
                .processor(doMainLogicItemProcessor)
                .writer(doMainLogicItemWriter)
                .listener(doMainLogicChunkListener)
                .faultTolerant()
                .retryPolicy(retryTemplate.retryPolicy())
                .backOffPolicy(retryTemplate.backOffPolicy())
                .build();
    }

    @Bean
    @Qualifier("B0002FinalizeJobStep")
    public Step B0002FinalizeJobStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0002FinalizeJobTasklet") AbstractFinalizeJobTasklet finalizeJobTasklet
    ) {
        return new StepBuilder("B0002FinalizeJobStep", jobRepository)
                .tasklet(finalizeJobTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0002FailureHandlingStep")
    public Step B0002FailureHandlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0002FailureHandlingTasklet") AbstractFailureHandlingTasklet failureHandlingTasklet
    ) {
        return new StepBuilder("B0002FailureHandlingStep", jobRepository)
                .tasklet(failureHandlingTasklet, transactionManager)
                .build();
    }
}