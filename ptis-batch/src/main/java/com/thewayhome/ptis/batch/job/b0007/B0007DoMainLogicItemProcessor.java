package com.thewayhome.ptis.batch.job.b0007;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.ParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0007DoMainLogicItemProcessor")
@StepScope
public class B0007DoMainLogicItemProcessor implements ItemProcessor<B0007DoMainLogicItemInput, B0007DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;
    private BusStationService busStationService;
    public B0007DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            ParamService paramService,
            ObjectMapper objectMapper,
            BusStationService busStationService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.paramService = paramService;
        this.objectMapper = objectMapper;
        this.busStationService = busStationService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
    @Override
    public B0007DoMainLogicItemOutput process(B0007DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        NodeRegisterRequestDto nodeRegisterReqDto = NodeRegisterRequestDto.builder()
                .nodeName(input.getBusStationName())
                .nodePosX(input.getBusStationPosX())
                .nodePosY(input.getBusStationPosY())
                .operatorId(jobName)
                .build();

        return B0007DoMainLogicItemOutput.builder()
                .busStationId(input.getBusStationId())
                .nodeRegisterReqDto(nodeRegisterReqDto)
                .build();
    }
}
