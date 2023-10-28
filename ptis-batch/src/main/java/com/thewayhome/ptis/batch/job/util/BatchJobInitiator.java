package com.thewayhome.ptis.batch.job.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class BatchJobInitiator {
    private final Job genesisJob;
    private final JobLauncher jobLauncher;

    @Autowired
    public BatchJobInitiator(
            @Qualifier("B0000Job") Job genesisJob,
            JobLauncher jobLauncher
    ) {
        this.genesisJob = genesisJob;
        this.jobLauncher = jobLauncher;
    }

    @PostConstruct
    public void executeJobOnStartup() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("jobName", "B0000")
                .addString("jobDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(genesisJob, jobParameters);

        if (jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            log.info("초기화 배치 작업이 성공적으로 완료되었습니다.");
        } else {
            log.info("초기화 배치 작업 실행 중 오류가 발생했습니다.");
        }
    }
}
