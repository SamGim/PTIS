package com.thewayhome.ptis.batch.job.b0009;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.batch.job.b0007.B0007DoMainLogicItemInput;
import com.thewayhome.ptis.batch.job.b0007.B0007DoMainLogicItemOutput;
import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.CompanyService;
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
@Qualifier("B0009DoMainLogicItemProcessor")
@StepScope
public class B0009DoMainLogicItemProcessor implements ItemProcessor<B0009DoMainLogicItemInput, B0009DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;

    public B0009DoMainLogicItemProcessor(
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
    public B0009DoMainLogicItemOutput process(B0009DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        NodeRegisterRequestDto nodeRegisterReqDto = NodeRegisterRequestDto.builder()
                .nodeName(input.getCompanyName())
                .nodePosX(input.getCompanyPosX())
                .nodePosY(input.getCompanyPosY())
                .operatorId(jobName)
                .build();

        return B0009DoMainLogicItemOutput.builder()
                .companyId(input.getCompanyId())
                .nodeRegisterReqDto(nodeRegisterReqDto)
                .build();
    }
}
