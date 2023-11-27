package com.thewayhome.ptis.batch.job.b0001;

import com.thewayhome.ptis.batch.job.base.AbstractFailureHandlingTasklet;
import com.thewayhome.ptis.batch.job.base.AbstractFinalizeJobTasklet;
import com.thewayhome.ptis.batch.job.base.AbstractInitValueTasklet;
import com.thewayhome.ptis.batch.job.base.AbstractValidateInputTasklet;
import com.thewayhome.ptis.batch.job.util.BatchJobStatusNotificationListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class B0001JobConfig {
    @Bean
    @Qualifier("B0001Job")
    public Job B0001Job(
            JobRepository jobRepository,
            @Qualifier("B0001ValidateInputStep") Step validateInput,
            @Qualifier("B0001InitValueStep") Step initValue,
            @Qualifier("B0001DoMainLogicStep") Step doMainLogic,
            @Qualifier("B0001FinalizeJobStep") Step finalizeJob,
            @Qualifier("B0001FailureHandlingStep") Step failureHandling,
            BatchJobStatusNotificationListener batchJobStatusNotificationListener
    ) {
        return new JobBuilder("B0001", jobRepository)
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
    @Qualifier("B0001ValidateInputStep")
    public Step B0001ValidateInputStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0001ValidateInputTasklet") AbstractValidateInputTasklet validateInputTasklet
    ) {
        return new StepBuilder("B0001ValidateInputStep", jobRepository)
                .tasklet(validateInputTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0001InitValueStep")
    public Step B0001InitValueStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0001InitValueTasklet") AbstractInitValueTasklet initValueTasklet
    ) {
        return new StepBuilder("B0001InitValueStep", jobRepository)
                .tasklet(initValueTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0001DoMainLogicStep")
    public Step B0001DoMainLogicStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0001DoMainLogicItemReader") ItemReader<B0001DoMainLogicItemInput> doMainLogicItemReader,
            @Qualifier("B0001DoMainLogicItemProcessor") ItemProcessor<B0001DoMainLogicItemInput, B0001DoMainLogicItemOutput> doMainLogicItemProcessor,
            @Qualifier("B0001DoMainLogicItemWriter") ItemWriter<B0001DoMainLogicItemOutput> doMainLogicItemWriter,
            B0001DoMainLogicRetryTemplate retryTemplate,
            B0001DoMainLogicChunkListener doMainLogicChunkListener
    ) {
        return new StepBuilder("B0001DoMainLogicStep", jobRepository)
                .<B0001DoMainLogicItemInput, B0001DoMainLogicItemOutput> chunk(100, transactionManager)
                .reader(doMainLogicItemReader)
                .processor(doMainLogicItemProcessor)
                .writer(doMainLogicItemWriter)
                .faultTolerant()
                .skipPolicy(retryTemplate.skipPolicy())
                .retryPolicy(retryTemplate.retryPolicy())
                .backOffPolicy(retryTemplate.backOffPolicy())
                .listener(doMainLogicChunkListener)
                .build();
    }

    @Bean
    @Qualifier("B0001FinalizeJobStep")
    public Step B0001FinalizeJobStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0001FinalizeJobTasklet") AbstractFinalizeJobTasklet finalizeJobTasklet
    ) {
        return new StepBuilder("B0001FinalizeJobStep", jobRepository)
                .tasklet(finalizeJobTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0001FailureHandlingStep")
    public Step B0001FailureHandlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0001FailureHandlingTasklet") AbstractFailureHandlingTasklet failureHandlingTasklet
    ) {
        return new StepBuilder("B0001FailureHandlingStep", jobRepository)
                .tasklet(failureHandlingTasklet, transactionManager)
                .build();
    }
}