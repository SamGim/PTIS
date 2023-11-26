package com.thewayhome.ptis.batch.job.b0003;

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
public class B0003JobConfig {
    @Bean
    @Qualifier("B0003Job")
    public Job B0003Job(
            JobRepository jobRepository,
            @Qualifier("B0003ValidateInputStep") Step validateInput,
            @Qualifier("B0003InitValueStep") Step initValue,
            @Qualifier("B0003DoMainLogicStep") Step doMainLogic,
            @Qualifier("B0003FinalizeJobStep") Step finalizeJob,
            @Qualifier("B0003FailureHandlingStep") Step failureHandling,
            BatchJobStatusNotificationListener batchJobStatusNotificationListener
    ) {
        return new JobBuilder("B0003", jobRepository)
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
    @Qualifier("B0003ValidateInputStep")
    public Step B0003ValidateInputStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0003ValidateInputTasklet") AbstractValidateInputTasklet validateInputTasklet
    ) {
        return new StepBuilder("B0003ValidateInputStep", jobRepository)
                .tasklet(validateInputTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0003InitValueStep")
    public Step B0003InitValueStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0003InitValueTasklet") AbstractInitValueTasklet initValueTasklet
    ) {
        return new StepBuilder("B0003InitValueStep", jobRepository)
                .tasklet(initValueTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0003DoMainLogicStep")
    public Step B0003DoMainLogicStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0003DoMainLogicItemReader") ItemReader<B0003DoMainLogicItemInput> doMainLogicItemReader,
            @Qualifier("B0003DoMainLogicItemProcessor") ItemProcessor<B0003DoMainLogicItemInput, B0003DoMainLogicItemOutput> doMainLogicItemProcessor,
            @Qualifier("B0003DoMainLogicItemWriter") ItemWriter<B0003DoMainLogicItemOutput> doMainLogicItemWriter,
            @Qualifier("B0003DoMainLogicChunkListener") ChunkListener doMainLogicChunkListener,
            B0003DoMainLogicRetryTemplate retryTemplate
    ) {
        return new StepBuilder("B0003DoMainLogicStep", jobRepository)
                .<B0003DoMainLogicItemInput, B0003DoMainLogicItemOutput> chunk(10, transactionManager)
                .reader(doMainLogicItemReader)
                .processor(doMainLogicItemProcessor)
                .writer(doMainLogicItemWriter)
                .listener(doMainLogicChunkListener)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(10)
                .retryPolicy(retryTemplate.retryPolicy())
                .backOffPolicy(retryTemplate.backOffPolicy())
                .listener(doMainLogicChunkListener)
                .build();
    }

    @Bean
    @Qualifier("B0003FinalizeJobStep")
    public Step B0003FinalizeJobStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0003FinalizeJobTasklet") AbstractFinalizeJobTasklet finalizeJobTasklet
    ) {
        return new StepBuilder("B0003FinalizeJobStep", jobRepository)
                .tasklet(finalizeJobTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0003FailureHandlingStep")
    public Step B0003FailureHandlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0003FailureHandlingTasklet") AbstractFailureHandlingTasklet failureHandlingTasklet
    ) {
        return new StepBuilder("B0003FailureHandlingStep", jobRepository)
                .tasklet(failureHandlingTasklet, transactionManager)
                .build();
    }
}