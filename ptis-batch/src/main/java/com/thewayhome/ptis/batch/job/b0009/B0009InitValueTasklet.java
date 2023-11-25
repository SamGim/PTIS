package com.thewayhome.ptis.batch.job.b0009;

import com.thewayhome.ptis.batch.job.base.AbstractInitValueTasklet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0009InitValueTasklet")
@StepScope
public class B0009InitValueTasklet extends AbstractInitValueTasklet {
    private StepExecution stepExecution;
    public B0009InitValueTasklet(
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

        return RepeatStatus.FINISHED;
    }
}

