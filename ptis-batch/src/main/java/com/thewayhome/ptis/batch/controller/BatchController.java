package com.thewayhome.ptis.batch.controller;

import com.thewayhome.ptis.batch.util.BatchJobManipulateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class BatchController {

    @Autowired
    private BatchJobManipulateUtil batchJobManipulateUtil;

    @Autowired
    private JobLauncher jobLauncher;

    @PostMapping("/batch/start")
    public String startBatchJob(
            @RequestParam String jobName,
            @RequestParam String jobDate,
            @RequestBody(required = false) Map<String, String> jobParamReq
    ) {
        JobParametersBuilder jobParamBuilder = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("jobName", jobName)
                .addString("jobDate", jobDate);

        if (jobParamReq != null) {
            for (Map.Entry<String, String> entry : jobParamReq.entrySet()) {
                jobParamBuilder.addString(entry.getKey(), entry.getValue());
            }
        }

        try {
            log.warn(jobParamBuilder.toJobParameters().toString());
            batchJobManipulateUtil.launchJob(jobName, jobParamBuilder.toJobParameters());
        } catch (Exception e) {
            return "Error starting the batch job " + jobName + ". " + e.getMessage();
        }

        return "Batch job " + jobName +" has been started.";
    }

    @PostMapping("/batch/stop")
    public String stopBatchJob(
            @RequestParam String jobName
    ) {
        batchJobManipulateUtil.stopJobExecution(jobName);
        return "Batch job " + jobName +" has been stopped.";
    }
}
