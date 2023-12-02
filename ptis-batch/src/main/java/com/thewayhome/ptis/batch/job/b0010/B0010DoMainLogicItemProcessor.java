package com.thewayhome.ptis.batch.job.b0010;

import com.thewayhome.ptis.batch.util.GeoUtils;
import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import com.thewayhome.ptis.core.service.LinkService;
import com.thewayhome.ptis.core.vo.LinkVo;
import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Qualifier("B0010DoMainLogicItemProcessor")
@StepScope
public class B0010DoMainLogicItemProcessor implements ItemProcessor<B0010DoMainLogicItemInput, B0010DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private final String walkSpeed;
    private final String isColdStart;
    private StepExecution stepExecution;
    private LinkService linkService;

    public B0010DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            @Value("#{jobParameters[walkSpeed]}") String walkSpeed,
            @Value("#{jobParameters[isColdStart]}") String isColdStart,
            LinkService linkService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.walkSpeed = walkSpeed;
        this.isColdStart = isColdStart;

        this.linkService = linkService;
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

        NodeVo srcNode = input.getSrcNode();
        NodeVo destNode = input.getDestNode();

        String srcNodeName = srcNode.getNodeName();
        String destNodeName = destNode.getNodeName();

        Long lnWalkSpeed = Long.parseLong(this.walkSpeed);

        long distance = GeoUtils.calculateDistance(srcNode.getNodePosX(), srcNode.getNodePosY(), destNode.getNodePosX(), destNode.getNodePosY(), true);
        long cost = GeoUtils.calculateTime(distance, lnWalkSpeed);

        LinkRegisterRequestDto req = LinkRegisterRequestDto.builder()
                .linkName(String.format("%s-%s", srcNodeName, destNodeName))
                .linkType("W")
                .stNode(input.getSrcNode())
                .edNode(input.getDestNode())
                .cost(cost)
                .operatorId(jobName)
                .build();

        if (!"Y".equalsIgnoreCase(this.isColdStart)) {
            // stNode, edNode, linkType이 동일한 노드가 이미 있는지확인해서 있다면 가져와서 업데이트한다.
            // 없다면 새로 생성한다.
            Optional<LinkVo> presentLink = linkService.findByStNodeAndEdNodeAndLinkType(srcNode, destNode, "W", this.jobName);

            if (presentLink.isPresent()) {
                LinkVo linkVo = presentLink.get();
                req.setId(linkVo.getId());
            }
        }

        return B0010DoMainLogicItemOutput.builder()
                .linkRegisterRequestDto(req)
                .build();
    }
}
