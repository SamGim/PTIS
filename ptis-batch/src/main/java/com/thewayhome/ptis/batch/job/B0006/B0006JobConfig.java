package com.thewayhome.ptis.batch.job.B0006;

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

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class B0006JobConfig {

    @Bean
    @Qualifier("B0006Job")
    public Job B0006Job(
            JobRepository jobRepository,
            @Qualifier("B0006ValidateInputStep") Step validateInput,
            @Qualifier("B0006InitValueStep") Step initValue,
            @Qualifier("B0006DoMainLogicStep") Step doMainLogic,
            @Qualifier("B0006FinalizeJobStep") Step finalizeJob,
            @Qualifier("B0006FailureHandlingStep") Step failureHandling,
            BatchJobStatusNotificationListener batchJobStatusNotificationListener

    ){
        return new JobBuilder("B0006", jobRepository)
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
    @Qualifier("B0006ValidateInputStep")
    public Step B0006ValidateInputStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0006ValidateInputTasklet") AbstractValidateInputTasklet validateInputTasklet
    ){
        return new StepBuilder("B0006ValidateInputStep", jobRepository)
                .tasklet(validateInputTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0006InitValueStep")
    public Step B0006InitValueStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0006InitValueTasklet") AbstractInitValueTasklet initValueTasklet
    ){
        return new StepBuilder("B0006InitValueStep", jobRepository)
                .tasklet(initValueTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0006DoMainLogicStep")
    public Step B0006DoMainLogicStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0006DoMainLogicItemReader") ItemReader<B0006DoMainLogicItemInput> doMainLogicItemReader,
            @Qualifier("B0006DoMainLogicItemProcessor") ItemProcessor<B0006DoMainLogicItemInput, B0006DoMainLogicItemOutput> doMainLogicItemProcessor,
            @Qualifier("B0006DoMainLogicItemWriter") ItemWriter<B0006DoMainLogicItemOutput> doMainLogicItemWriter,
            B0006DoMainLogicRetryTemplate retryTemplate,
            B0006DoMainLogicChunkListener doMainLogicChunkListener
    ){
        return new StepBuilder("B0006DoMainLogicStep", jobRepository)
                .<B0006DoMainLogicItemInput, B0006DoMainLogicItemOutput> chunk(100, transactionManager)
                .reader(doMainLogicItemReader)
                .processor(doMainLogicItemProcessor)
                .writer(doMainLogicItemWriter)
                .faultTolerant()
                .retry(IOException.class)
                .retryPolicy(retryTemplate.retryPolicy())
                .backOffPolicy(retryTemplate.backOffPolicy())
                .listener(doMainLogicChunkListener)
                .build();
    }

    @Bean
    @Qualifier("B0006FinalizeJobStep")
    public Step B0006FinalizeJobStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0006FinalizeJobTasklet") AbstractFinalizeJobTasklet finalizeJobTasklet
    ){
        return new StepBuilder("B0006FinalizeJobStep", jobRepository)
                .tasklet(finalizeJobTasklet, transactionManager)
                .build();
    }

    @Bean
    @Qualifier("B0006FailureHandlingStep")
    public Step B0006FailureHandlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("B0006FailureHandlingTasklet") AbstractFailureHandlingTasklet failureHandlingTasklet
    ){
        return new StepBuilder("B0006FailureHandlingStep", jobRepository)
                .tasklet(failureHandlingTasklet, transactionManager)
                .build();
    }

}
