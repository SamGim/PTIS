package com.thewayhome.ptis.batch.job.b0000;

import com.thewayhome.ptis.batch.job.base.AbstractDoMainLogicTasklet;
import com.thewayhome.ptis.core.dto.request.ParamsRegisterRequestDto;
import com.thewayhome.ptis.core.service.ParamService;
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

    private final String apiEndpoint;
    private final ParamService paramService;

    public B0000DoMainLogicTasklet(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            @Value("${server.endpoint.api}") String apiEndpoint,
            ParamService paramService
    ) {
        super(jobName, jobDate);
        this.apiEndpoint = apiEndpoint;
        this.paramService = paramService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        String jobName = this.getJobName();
        String jobDate = this.getJobDate();
        String taskletName = this.getTaskletName();

        log.info("[" + taskletName + "] jobName = " + jobName);
        log.info("[" + taskletName + "] jobDate = " + jobDate);

        ParamsRegisterRequestDto B0001InputParams = ParamsRegisterRequestDto.builder()
                .groupName(ParamService.BATCH_JOB_INPUT_PARAM_GROUP_NAME)
                .paramName("B0001")
                .value("R|ws_bus_st_20230914.csv|,")
                .useYn("Y")
                .operatorId(jobName)
                .build();
        paramService.registerParam(B0001InputParams);

        ParamsRegisterRequestDto B0002InputParams = ParamsRegisterRequestDto.builder()
                .groupName(ParamService.BATCH_JOB_INPUT_PARAM_GROUP_NAME)
                .paramName("B0002")
                .value(this.apiEndpoint+"|/ws-pure/getRouteByStationList")
                .useYn("Y")
                .operatorId(jobName)
                .build();
        paramService.registerParam(B0002InputParams);

        ParamsRegisterRequestDto B0003InputParams = ParamsRegisterRequestDto.builder()
                .groupName(ParamService.BATCH_JOB_INPUT_PARAM_GROUP_NAME)
                .paramName("B0003")
                .value(this.apiEndpoint+"|/ws-pure/getStationsByRouteList")
                .useYn("Y")
                .operatorId(jobName)
                .build();
        paramService.registerParam(B0003InputParams);

        ParamsRegisterRequestDto B0004InputParams = ParamsRegisterRequestDto.builder()
                .groupName(ParamService.BATCH_JOB_INPUT_PARAM_GROUP_NAME)
                .paramName("B0004")
                .value("J|seoul_cafe_and_restaurant_20231101.json")
                .useYn("Y")
                .operatorId(jobName)
                .build();
        paramService.registerParam(B0004InputParams);

        ParamsRegisterRequestDto B0006InputParams = ParamsRegisterRequestDto.builder()
                .groupName(ParamService.BATCH_JOB_INPUT_PARAM_GROUP_NAME)
                .paramName("B0006")
                .value("J|seoul_gym_20231106.json")
                .useYn("Y")
                .operatorId(jobName)
                .build();
        paramService.registerParam(B0006InputParams);

        ParamsRegisterRequestDto S0001InputParams = ParamsRegisterRequestDto.builder()
                .groupName(ParamService.SERVICE_INPUT_PARAM_GROUP_NAME)
                .paramName("S0001")
                .value(this.apiEndpoint+"|/ws-pure/getStationsByPosList")
                .useYn("Y")
                .operatorId(jobName)
                .build();
        paramService.registerParam(S0001InputParams);

        return RepeatStatus.FINISHED;
    }
}

