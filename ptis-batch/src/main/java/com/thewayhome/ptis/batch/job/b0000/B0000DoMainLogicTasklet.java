package com.thewayhome.ptis.batch.job.b0000;

import com.thewayhome.ptis.batch.job.base.AbstractDoMainLogicTasklet;
import com.thewayhome.ptis.batch.service.ParamService;
import com.thewayhome.ptis.batch.vo.ParamsRegisterReqVo;
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

    private final ParamService paramService;

    public B0000DoMainLogicTasklet(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            ParamService paramService
    ) {
        super(jobName, jobDate);
        this.paramService = paramService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        String jobName = this.getJobName();
        String jobDate = this.getJobDate();
        String taskletName = this.getTaskletName();

        log.info("[" + taskletName + "] jobName = " + jobName);
        log.info("[" + taskletName + "] jobDate = " + jobDate);

        ParamsRegisterReqVo B0001InputParams = new ParamsRegisterReqVo();
        B0001InputParams.setGroupName(ParamService.BATCH_JOB_INPUT_PARAM_GROUP_NAME);
        B0001InputParams.setParamName("B0001");
        B0001InputParams.setValue("R|ws_bus_st_20230914.csv|,");
        B0001InputParams.setUseYn("Y");
        B0001InputParams.setOperatorId(jobName);
        paramService.saveParam(B0001InputParams);

        ParamsRegisterReqVo B0002InputParams = new ParamsRegisterReqVo();
        B0002InputParams.setGroupName(ParamService.BATCH_JOB_INPUT_PARAM_GROUP_NAME);
        B0002InputParams.setParamName("B0002");
        B0002InputParams.setValue("http://localhost:8081|/ws-pure/getRouteByStationList");
        B0002InputParams.setUseYn("Y");
        B0002InputParams.setOperatorId(jobName);
        paramService.saveParam(B0002InputParams);

        return RepeatStatus.FINISHED;
    }
}

