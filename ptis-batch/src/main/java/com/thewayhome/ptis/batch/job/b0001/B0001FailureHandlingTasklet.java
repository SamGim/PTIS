package com.thewayhome.ptis.batch.job.b0001;

import com.thewayhome.ptis.batch.job.base.AbstractFailureHandlingTasklet;
import com.thewayhome.ptis.batch.util.BatchJobSlackMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0001FailureHandlingTasklet")
@StepScope
public class B0001FailureHandlingTasklet extends AbstractFailureHandlingTasklet {
    private StepExecution stepExecution;
    public B0001FailureHandlingTasklet(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate
    ) {
        super(jobName, jobDate);
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws JobExecutionException {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        String jobName = this.getJobName();
        String jobDate = this.getJobDate();
        String taskletName = this.getTaskletName();

        log.info("[" + taskletName + "] jobName = " + jobName);
        log.info("[" + taskletName + "] jobDate = " + jobDate);

        throw new JobExecutionException("Something went wrong in Job [" + jobName + "]");
    }
}
