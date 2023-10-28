package com.thewayhome.ptis.batch.job.util;

import com.thewayhome.ptis.batch.util.BatchJobSlackMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchJobStatusNotificationListener implements JobExecutionListener {

    private final BatchJobSlackMessageUtil batchJobSlackMessageUtil;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        batchJobSlackMessageUtil.emitBatchJobStatusChangedEvent(
                jobExecution.getJobParameters().getString("jobName"),
                jobExecution.getJobParameters().getString("jobDate"),
                jobExecution.getStatus()
        );
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        batchJobSlackMessageUtil.emitBatchJobStatusChangedEvent(
                jobExecution.getJobParameters().getString("jobName"),
                jobExecution.getJobParameters().getString("jobDate"),
                jobExecution.getStatus()
        );
    }
}
