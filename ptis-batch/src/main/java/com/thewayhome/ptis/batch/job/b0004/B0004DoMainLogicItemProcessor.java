package com.thewayhome.ptis.batch.job.b0004;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0004DoMainLogicItemProcessor")
@StepScope
public class B0004DoMainLogicItemProcessor implements ItemProcessor<B0004DoMainLogicItemInput, B0004DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    public B0004DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
    }
    @Override
    public B0004DoMainLogicItemOutput process(B0004DoMainLogicItemInput input) {
        final String nodeId = input.getNodeId();
        final String nodeAddress = input.getNodeAddress();
        final String nodePosX = input.getNodePosX();
        final String nodePosY = input.getNodePosY();
        final String nodeType = input.getNodeType();
        final String nodeName = input.getNodeName();

        return B0004DoMainLogicItemOutput.builder()
                .nodeId(nodeId)
                .nodeAddress(nodeAddress)
                .nodePosX(nodePosX)
                .nodePosY(nodePosY)
                .nodeType(nodeType)
                .nodeName(nodeName)
                .build();
    }
}
