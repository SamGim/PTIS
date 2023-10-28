package com.thewayhome.ptis.batch.job.b0001;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0001DoMainLogicItemProcessor")
@StepScope
public class B0001DoMainLogicItemProcessor implements ItemProcessor<B0001DoMainLogicItemInput, B0001DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    public B0001DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
    }
    @Override
    public B0001DoMainLogicItemOutput process(B0001DoMainLogicItemInput input) {
        final String nodeId = input.getNodeId();
        final String arsId = input.getArsId();
        final String nodeName = input.getNodeName();
        final String nodePosX = input.getNodePosX();
        final String nodePosY = input.getNodePosY();
        final String nodePosType = input.getNodePosType();

        final B0001DoMainLogicItemOutput output = B0001DoMainLogicItemOutput.builder()
                .nodeId(nodeId)
                .arsId(arsId)
                .nodeName(nodeName)
                .nodePosX(nodePosX)
                .nodePosY(nodePosY)
                .nodePosType(nodePosType)
                .build();

        return output;
    }
}
