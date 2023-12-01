package com.thewayhome.ptis.batch.job.b0020;

import com.thewayhome.ptis.batch.job.b0010.B0010DoMainLogicItemInput;
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
@Qualifier("B0020DoMainLogicItemReader")
@StepScope
public class B0020DoMainLogicItemReader implements ItemStreamReader<B0020DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private final String srcNodeIdSt;
    private final String srcNodeIdEd;
    private final String destNodeIdSt;
    private final String destNodeIdEd;
    private StepExecution stepExecution;
    private final List<NodeVo> items;
    private final NodeService nodeService;

    private int iIndex = 0;
    private int kIndex = 0;
    private int maxIndex = 0;

    public B0020DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            @Value("#{jobParameters[srcNodeIdSt]}") String srcNodeIdSt,
            @Value("#{jobParameters[srcNodeIdEd]}") String srcNodeIdEd,
            @Value("#{jobParameters[destNodeIdSt]}") String destNodeIdSt,
            @Value("#{jobParameters[destNodeIdEd]}") String destNodeIdEd,
            NodeService nodeService
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.srcNodeIdSt = srcNodeIdSt;
        this.srcNodeIdEd = srcNodeIdEd;
        this.destNodeIdSt = destNodeIdSt;
        this.destNodeIdEd = destNodeIdEd;
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

        List<NodeVo> items = nodeService.findAll(jobName);
        this.maxIndex = items.size();
    }

    @Override
    public B0020DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }
        NodeVo curINode = items.get(iIndex);
        NodeVo curKNode = items.get(kIndex);

        if (kIndex == maxIndex) {
            kIndex = 0;
            iIndex++;
            if (iIndex > maxIndex) {
                return null;
            }
        }
        else {
            kIndex++;
        }
        return B0020DoMainLogicItemInput.builder()
                .iNode(curINode)
                .kNode(curKNode)
                .build();
    }
}
