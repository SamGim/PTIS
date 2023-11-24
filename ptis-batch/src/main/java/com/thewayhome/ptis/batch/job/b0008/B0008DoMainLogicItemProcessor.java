package com.thewayhome.ptis.batch.job.b0008;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
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
@Qualifier("B0008DoMainLogicItemProcessor")
@StepScope
public class B0008DoMainLogicItemProcessor implements ItemProcessor<B0008DoMainLogicItemInput, B0008DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;

    public B0008DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            ParamService paramService,
            ObjectMapper objectMapper
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.paramService = paramService;
        this.objectMapper = objectMapper;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
    @Override
    public B0008DoMainLogicItemOutput process(B0008DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        NodeRegisterRequestDto nodeRegisterReqDto = NodeRegisterRequestDto.builder()
                .nodeName(input.getComplexName())
                .nodePosX(input.getComplexPosX())
                .nodePosY(input.getComplexPosY())
                .operatorId(jobName)
                .build();

        return B0008DoMainLogicItemOutput.builder()
                .complexId(input.getComplexId())
                .nodeRegisterReqDto(nodeRegisterReqDto)
                .build();
    }
}
