package com.thewayhome.ptis.batch.job.b0010;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.batch.util.GeoUtils;
import com.thewayhome.ptis.core.dto.LinkRegisterReqDto;
import com.thewayhome.ptis.core.entity.Node;
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
@Qualifier("B0010DoMainLogicItemProcessor")
@StepScope
public class B0010DoMainLogicItemProcessor implements ItemProcessor<B0010DoMainLogicItemInput, B0010DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;
    private BusStationService busStationService;
    public B0010DoMainLogicItemProcessor(
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
    public B0010DoMainLogicItemOutput process(B0010DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        Node srcNode = input.getSrcNode();
        Node destNode = input.getDestNode();

        String srcNodeName = srcNode.getNodeName();
        String destNodeName = destNode.getNodeName();

        long distance = GeoUtils.calculateDistance(srcNode.getNodePosX(), srcNode.getNodePosY(), destNode.getNodePosX(), destNode.getNodePosY(), true);
        long cost = GeoUtils.calculateTime(distance, 3);

        LinkRegisterReqDto linkRegisterReqDto = new LinkRegisterReqDto();
        linkRegisterReqDto.setLinkName(String.format("%s-%s", srcNodeName, destNodeName));
        linkRegisterReqDto.setLinkType("W");
        linkRegisterReqDto.setStNode(input.getSrcNode());
        linkRegisterReqDto.setEdNode(input.getDestNode());
        linkRegisterReqDto.setCost(cost);
        linkRegisterReqDto.setOperatorId(jobName);

        return B0010DoMainLogicItemOutput.builder()
                .linkRegisterReqDto(linkRegisterReqDto)
                .build();
    }
}
