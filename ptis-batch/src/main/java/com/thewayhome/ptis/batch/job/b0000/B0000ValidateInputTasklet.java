package com.thewayhome.ptis.batch.job.b0000;

import com.thewayhome.ptis.batch.job.base.AbstractValidateInputTasklet;
import com.thewayhome.ptis.batch.util.BatchJobSlackMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0000ValidateInputTasklet")
@StepScope
public class B0000ValidateInputTasklet extends AbstractValidateInputTasklet {
    public B0000ValidateInputTasklet(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate
    ) {
        super(jobName, jobDate);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String jobName = this.getJobName();
        String jobDate = this.getJobDate();
        String taskletName = this.getTaskletName();

        log.info("[" + taskletName + "] jobName = " + jobName);
        log.info("[" + taskletName + "] jobDate = " + jobDate);

        if (jobName.trim().isEmpty()) {
            throw new IllegalArgumentException("JobName 필수");
        }

        if (jobDate.trim().isEmpty()) {
            throw new IllegalArgumentException("JobDate 필수");
        }

        if (taskletName.isEmpty()) {
            throw new IllegalArgumentException("taskletName 필수");
        }

        return RepeatStatus.FINISHED;
    }
}

