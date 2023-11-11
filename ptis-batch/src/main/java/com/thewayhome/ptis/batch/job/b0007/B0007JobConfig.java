package com.thewayhome.ptis.batch.job.b0007;

import com.thewayhome.ptis.batch.job.base.AbstractFailureHandlingTasklet;
import com.thewayhome.ptis.batch.job.base.AbstractFinalizeJobTasklet;
import com.thewayhome.ptis.batch.job.base.AbstractInitValueTasklet;
import com.thewayhome.ptis.batch.job.base.AbstractValidateInputTasklet;
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
public class B0007JobConfig {
    @Bean
    @Qualifier("B0007Job")
    public Job B0007Job(
            JobRepository jobRepository,
            @Qualifier("B0007ValidateInputStep") Step validateInput,
            @Qualifier("B0007InitValueStep") Step initValue,
            @Qualifier("B0007DoMainLogicStep") Step doMainLogic,
            @Qualifier("B0007FinalizeJobStep") Step finalizeJob,
            @Qualifier("B0007FailureHandlingStep") Step failureHandling,
            BatchJobStatusNotificationListener batchJobStatusNotificationListener
    ) {
        return new JobBuilder("B0007", jobRepository)
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
    @Qualifier("B0007ValidateInputStep")
    public Step B0007ValidateInputStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0007ValidateInputTasklet") AbstractValidateInputTasklet validateInputTasklet
    ) {
        return new StepBuilder("B0007ValidateInputStep", jobRepository)
                .tasklet(validateInputTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0007InitValueStep")
    public Step B0007InitValueStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0007InitValueTasklet") AbstractInitValueTasklet initValueTasklet
    ) {
        return new StepBuilder("B0007InitValueStep", jobRepository)
                .tasklet(initValueTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0007DoMainLogicStep")
    public Step B0007DoMainLogicStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0007DoMainLogicItemReader") ItemReader<B0007DoMainLogicItemInput> doMainLogicItemReader,
            @Qualifier("B0007DoMainLogicItemProcessor") ItemProcessor<B0007DoMainLogicItemInput, B0007DoMainLogicItemOutput> doMainLogicItemProcessor,
            @Qualifier("B0007DoMainLogicItemWriter") ItemWriter<B0007DoMainLogicItemOutput> doMainLogicItemWriter,
            @Qualifier("B0007DoMainLogicChunkListener") ChunkListener doMainLogicChunkListener,
            B0007DoMainLogicRetryTemplate retryTemplate
    ) {
        return new StepBuilder("B0007DoMainLogicStep", jobRepository)
                .<B0007DoMainLogicItemInput, B0007DoMainLogicItemOutput> chunk(100, transactionManager)
                .reader(doMainLogicItemReader)
                .processor(doMainLogicItemProcessor)
                .writer(doMainLogicItemWriter)
                .listener(doMainLogicChunkListener)
                .faultTolerant()
                .retryPolicy(retryTemplate.retryPolicy())
                .backOffPolicy(retryTemplate.backOffPolicy())
                .listener(doMainLogicChunkListener)
                .build();
    }

    @Bean
    @Qualifier("B0007FinalizeJobStep")
    public Step B0007FinalizeJobStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0007FinalizeJobTasklet") AbstractFinalizeJobTasklet finalizeJobTasklet
    ) {
        return new StepBuilder("B0007FinalizeJobStep", jobRepository)
                .tasklet(finalizeJobTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0007FailureHandlingStep")
    public Step B0007FailureHandlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0007FailureHandlingTasklet") AbstractFailureHandlingTasklet failureHandlingTasklet
    ) {
        return new StepBuilder("B0007FailureHandlingStep", jobRepository)
                .tasklet(failureHandlingTasklet, transactionManager)
                .build();
    }
}