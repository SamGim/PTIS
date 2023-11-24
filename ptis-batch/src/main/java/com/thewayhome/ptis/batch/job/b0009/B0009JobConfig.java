package com.thewayhome.ptis.batch.job.b0009;

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
public class B0009JobConfig {
    @Bean
    @Qualifier("B0009Job")
    public Job B0009Job(
            JobRepository jobRepository,
            @Qualifier("B0009ValidateInputStep") Step validateInput,
            @Qualifier("B0009InitValueStep") Step initValue,
            @Qualifier("B0009DoMainLogicStep") Step doMainLogic,
            @Qualifier("B0009FinalizeJobStep") Step finalizeJob,
            @Qualifier("B0009FailureHandlingStep") Step failureHandling,
            BatchJobStatusNotificationListener batchJobStatusNotificationListener
    ) {
        return new JobBuilder("B0009", jobRepository)
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
    @Qualifier("B0009ValidateInputStep")
    public Step B0009ValidateInputStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0009ValidateInputTasklet") AbstractValidateInputTasklet validateInputTasklet
    ) {
        return new StepBuilder("B0009ValidateInputStep", jobRepository)
                .tasklet(validateInputTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0009InitValueStep")
    public Step B0009InitValueStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0009InitValueTasklet") AbstractInitValueTasklet initValueTasklet
    ) {
        return new StepBuilder("B0009InitValueStep", jobRepository)
                .tasklet(initValueTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0009DoMainLogicStep")
    public Step B0009DoMainLogicStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0009DoMainLogicItemReader") ItemReader<B0009DoMainLogicItemInput> doMainLogicItemReader,
            @Qualifier("B0009DoMainLogicItemProcessor") ItemProcessor<B0009DoMainLogicItemInput, B0009DoMainLogicItemOutput> doMainLogicItemProcessor,
            @Qualifier("B0009DoMainLogicItemWriter") ItemWriter<B0009DoMainLogicItemOutput> doMainLogicItemWriter,
            @Qualifier("B0009DoMainLogicChunkListener") ChunkListener doMainLogicChunkListener,
            B0009DoMainLogicRetryTemplate retryTemplate
    ) {
        return new StepBuilder("B0009DoMainLogicStep", jobRepository)
                .<B0009DoMainLogicItemInput, B0009DoMainLogicItemOutput> chunk(100, transactionManager)
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
    @Qualifier("B0009FinalizeJobStep")
    public Step B0009FinalizeJobStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0009FinalizeJobTasklet") AbstractFinalizeJobTasklet finalizeJobTasklet
    ) {
        return new StepBuilder("B0009FinalizeJobStep", jobRepository)
                .tasklet(finalizeJobTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0009FailureHandlingStep")
    public Step B0009FailureHandlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0009FailureHandlingTasklet") AbstractFailureHandlingTasklet failureHandlingTasklet
    ) {
        return new StepBuilder("B0009FailureHandlingStep", jobRepository)
                .tasklet(failureHandlingTasklet, transactionManager)
                .build();
    }
}