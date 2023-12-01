package com.thewayhome.ptis.batch.job.b0020;

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
public class B0010JobConfig {
    @Bean
    @Qualifier("B0010Job")
    public Job B0010Job(
            JobRepository jobRepository,
            @Qualifier("B0010ValidateInputStep") Step validateInput,
            @Qualifier("B0010InitValueStep") Step initValue,
            @Qualifier("B0010DoMainLogicStep") Step doMainLogic,
            @Qualifier("B0010FinalizeJobStep") Step finalizeJob,
            @Qualifier("B0010FailureHandlingStep") Step failureHandling,
            BatchJobStatusNotificationListener batchJobStatusNotificationListener
    ) {
        return new JobBuilder("B0010", jobRepository)
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
    @Qualifier("B0010ValidateInputStep")
    public Step B0010ValidateInputStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0020ValidateInputTasklet") AbstractValidateInputTasklet validateInputTasklet
    ) {
        return new StepBuilder("B0010ValidateInputStep", jobRepository)
                .tasklet(validateInputTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0010InitValueStep")
    public Step B0010InitValueStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0020InitValueTasklet") AbstractInitValueTasklet initValueTasklet
    ) {
        return new StepBuilder("B0010InitValueStep", jobRepository)
                .tasklet(initValueTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0010DoMainLogicStep")
    public Step B0010DoMainLogicStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0020DoMainLogicItemReader") ItemReader<B0020DoMainLogicItemInput> doMainLogicItemReader,
            @Qualifier("B0020DoMainLogicItemProcessor") ItemProcessor<B0020DoMainLogicItemInput, B0020DoMainLogicItemOutput> doMainLogicItemProcessor,
            @Qualifier("B0010DoMainLogicItemWriter") ItemWriter<B0020DoMainLogicItemOutput> doMainLogicItemWriter,
            @Qualifier("B0020DoMainLogicChunkListener") ChunkListener doMainLogicChunkListener,
            B0020DoMainLogicRetryTemplate retryTemplate
    ) {
        return new StepBuilder("B0010DoMainLogicStep", jobRepository)
                .<B0020DoMainLogicItemInput, B0020DoMainLogicItemOutput> chunk(100, transactionManager)
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
    @Qualifier("B0010FinalizeJobStep")
    public Step B0010FinalizeJobStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0020FinalizeJobTasklet") AbstractFinalizeJobTasklet finalizeJobTasklet
    ) {
        return new StepBuilder("B0010FinalizeJobStep", jobRepository)
                .tasklet(finalizeJobTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0010FailureHandlingStep")
    public Step B0010FailureHandlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0020FailureHandlingTasklet") AbstractFailureHandlingTasklet failureHandlingTasklet
    ) {
        return new StepBuilder("B0010FailureHandlingStep", jobRepository)
                .tasklet(failureHandlingTasklet, transactionManager)
                .build();
    }
}