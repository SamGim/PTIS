package com.thewayhome.ptis.batch.job.b0010;

import com.thewayhome.ptis.core.service.NodeService;
import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("B0010DoMainLogicItemReader")
@StepScope
public class B0010DoMainLogicItemReader implements ItemStreamReader<B0010DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private final String isColdStart;
    private StepExecution stepExecution;
    private final List<NodeVo> items;
    private final NodeService nodeService;

    private int srcIndex = 0;
    private int destIndex = 0;
    private int maxIndex = 0;

    public B0010DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            @Value("#{jobParameters[isColdStart]}") String isColdStart,
            NodeService nodeService
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.isColdStart = isColdStart;

        this.nodeService = nodeService;

        this.items = new ArrayList<>();
        initialize();
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    public void initialize() throws IndexOutOfBoundsException, JobInterruptedException {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        this.items.addAll(nodeService.findAll(jobName));
        this.maxIndex = items.size() - 1;
    }

    @Override
    public B0010DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }

        if (srcIndex > maxIndex) {
            return null;
        }

        NodeVo curSrcNode = items.get(srcIndex);
        NodeVo curDestNode = items.get(destIndex);

        if (destIndex == maxIndex) {
            destIndex = 0;
            srcIndex++;
        }
        else {
            destIndex++;
        }
        return B0010DoMainLogicItemInput.builder()
                .srcNode(curSrcNode)
                .destNode(curDestNode)
                .build();
    }
}
