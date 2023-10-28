package com.thewayhome.ptis.batch.job.b0000;

import com.thewayhome.ptis.batch.job.base.AbstractDoMainLogicTasklet;
import com.thewayhome.ptis.batch.service.BatchJobService;
import com.thewayhome.ptis.batch.vo.BatchJobRegisterReqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Qualifier("B0000DoMainLogicTasklet")
@StepScope
public class B0000DoMainLogicTasklet extends AbstractDoMainLogicTasklet {

    private final BatchJobService batchJobService;

    public B0000DoMainLogicTasklet(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            BatchJobService batchJobService
    ) {
        super(jobName, jobDate);
        this.batchJobService = batchJobService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        String jobName = this.getJobName();
        String jobDate = this.getJobDate();
        String taskletName = this.getTaskletName();

        log.info("[" + taskletName + "] jobName = " + jobName);
        log.info("[" + taskletName + "] jobDate = " + jobDate);

        BatchJobRegisterReqVo batchJob_B0001 = new BatchJobRegisterReqVo();
        batchJob_B0001.setName("B0001");
        batchJob_B0001.setInputType("R");
        batchJob_B0001.setInputFilename("ws_bus_st_20230914.csv");
        batchJob_B0001.setInputDelimiter(",");
        batchJob_B0001.setUseYn("Y");
        batchJob_B0001.setOperatorId(jobName);
        batchJobService.saveBatchJob(batchJob_B0001);

        BatchJobRegisterReqVo batchJob_B0002 = new BatchJobRegisterReqVo();
        batchJob_B0002.setName("B0002");
        batchJob_B0002.setInputType(" ");
        batchJob_B0002.setInputFilename(" ");
        batchJob_B0002.setInputDelimiter(" ");
        batchJob_B0002.setUseYn("Y");
        batchJob_B0002.setOperatorId(jobName);
        batchJobService.saveBatchJob(batchJob_B0002);

        return RepeatStatus.FINISHED;
    }
}

