package com.thewayhome.ptis.batch.job.b0008;

import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import com.thewayhome.ptis.core.service.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0008DoMainLogicItemWriter")
@StepScope
public class B0008DoMainLogicItemWriter implements ItemWriter<B0008DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final NodeService nodeService;

    public B0008DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            NodeService nodeService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.nodeService = nodeService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends B0008DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0008DoMainLogicItemOutput item : chunk.getItems()) {
            NodeRegisterRequestDto nodeRegisterReqDto = item.getNodeRegisterReqDto();
            Long complexId = item.getComplexId();

//            nodeService.createNodeFromComplex(nodeRegisterReqDto, complexId);
        }
    }
}
