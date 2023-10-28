package com.thewayhome.ptis.batch.service;

import com.thewayhome.ptis.batch.repository.BatchJobRepository;
import com.thewayhome.ptis.batch.vo.BatchJob;
import com.thewayhome.ptis.batch.vo.BatchJobRegisterReqVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchJobService {
    private final BatchJobRepository batchJobRepository;
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JobOperator jobOperator;

    public Optional<BatchJob> findBatchJob(String jobName) {
        return batchJobRepository.findById(jobName);
    }

    @Async("jobExecutor")
    public void stopJobExecution(String jobName) {
        Set<Long> executions = null;
        try {
            executions = jobOperator.getRunningExecutions(jobName);
            for (Long execution : executions) {
                stopJobExecution(execution);
            }
        } catch (NoSuchJobException e) {
            log.warn("작업명" + jobName + " 중지 실패");
        }
    }

    @Async("jobExecutor")
    public void stopJobExecution(Long executionId) {
        try {
            log.info("Stopping Job: " + executionId);
            jobOperator.stop(executionId);
        } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
            log.warn("작업 ID " + executionId + " 중지 실패");
        }
    }

    @Async("jobExecutor")
    public void launchJob(String jobName, JobParameters jobParameters) throws Exception {
        Job job = jobRegistry.getJob(jobName);
        jobLauncher.run(job, jobParameters);
    }

    public BatchJob saveBatchJob(BatchJobRegisterReqVo req) {
        BatchJob batchJob = batchJobRepository.findById(req.getName()).orElse(new BatchJob());

        // DATA
        batchJob.setName(req.getName());
        batchJob.setInputType(req.getInputType());
        batchJob.setInputFilename(req.getInputFilename());
        batchJob.setInputDelimiter(req.getInputDelimiter());
        batchJob.setUseYn(req.getUseYn());

        // DB
        if (batchJobRepository.findById(batchJob.getName()).isEmpty()) {
            batchJob.setCreatedAt(LocalDateTime.now());
            batchJob.setCreatedBy(req.getOperatorId());
        }
        batchJob.setUpdatedAt(LocalDateTime.now());
        batchJob.setUpdatedBy(req.getOperatorId());

        return batchJobRepository.save(batchJob);
    }
}
