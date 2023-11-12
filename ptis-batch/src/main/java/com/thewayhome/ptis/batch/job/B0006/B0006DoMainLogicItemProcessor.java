package com.thewayhome.ptis.batch.job.B0006;

import com.thewayhome.ptis.batch.job.b0004.B0004DoMainLogicItemInput;
import com.thewayhome.ptis.batch.job.b0004.B0004DoMainLogicItemOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0006DoMainLogicItemProcessor")
@StepScope
public class B0006DoMainLogicItemProcessor implements ItemProcessor<B0006DoMainLogicItemInput, B0006DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    public B0006DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
    }
    @Override
    public B0006DoMainLogicItemOutput process(B0006DoMainLogicItemInput input) {
        final String nodeId = input.getNodeId();
        final String nodeAddress = input.getNodeAddress();
        final String nodePosX = input.getNodePosX();
        final String nodePosY = input.getNodePosY();
        final String nodeName = input.getNodeName();

        return B0006DoMainLogicItemOutput.builder()
                .nodeId(nodeId)
                .nodeAddress(nodeAddress)
                .nodePosX(nodePosX)
                .nodePosY(nodePosY)
                .nodeName(nodeName)
                .build();
    }
}
