package com.thewayhome.ptis.batch.job.b0020;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.batch.job.b0010.B0010DoMainLogicItemInput;
import com.thewayhome.ptis.batch.job.b0010.B0010DoMainLogicItemOutput;
import com.thewayhome.ptis.batch.util.GeoUtils;
import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import com.thewayhome.ptis.core.entity.ShortestPathLink;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.NodeService;
import com.thewayhome.ptis.core.service.ParamService;
import com.thewayhome.ptis.core.service.ShortestPathLinkService;
import com.thewayhome.ptis.core.vo.NodeVo;
import com.thewayhome.ptis.core.vo.ShortestPathLinkVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Qualifier("B0020DoMainLogicItemProcessor")
@StepScope
public class B0020DoMainLogicItemProcessor implements ItemProcessor<B0020DoMainLogicItemInput, B0020DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final NodeService nodeService;
    private final ShortestPathLinkService shortestPathLinkService;
    public B0020DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            NodeService nodeService,
            ShortestPathLinkService shortestPathLinkService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.nodeService = nodeService;
        this.shortestPathLinkService = shortestPathLinkService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
    @Override
    public B0020DoMainLogicItemOutput process(B0020DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }
        // I노드부터 모든 노드까지 K노드를 거칠경우를 가정해 최단 소요시간을 계산한다.
        // I노드를 가져온다.
        // 모든 노드J를 가져온다.
        // I노드부터 J노드의 SPL를 가져오고 없으면 Link중 최단 시간으로 SPL을 만든다.
        // 기존 SPL의 소요시간값과 SPL(i, k) + SPL(k, j)의 소요시간값을 비교하여 작은 값을 선택한다. 이 경우도 SPL이 없다면 Link중 최단 시간으로 SPL을 만든다.
        // 모든 노드J에 대해 위의 과정을 반복한다.
        List<NodeVo> items = nodeService.findAll(jobName);
        NodeVo iNode = input.getINode();
        NodeVo kNode = input.getKNode();
        for (NodeVo jNode : items){
            ShortestPathLinkVo curSpl = shortestPathLinkService.findBySrcNodeIdAndDestNodeId(iNode, jNode, jobName);
            ShortestPathLinkVo ikSpl = shortestPathLinkService.findBySrcNodeIdAndDestNodeId(iNode, kNode, jobName);
            ShortestPathLinkVo kjSpl = shortestPathLinkService.findBySrcNodeIdAndDestNodeId(kNode, jNode, jobName);

            if (curSpl.getCost() > ikSpl.getCost() + kjSpl.getCost()){
                curSpl.setCost(ikSpl.getCost() + kjSpl.getCost());
                curSpl.setPrevNode(kNode);
                // 여기서 이렇게 저장하면 새로운게 생긴다. write에서 저장해야함 글고
                shortestPathLinkService.registerShortestPathLink(curSpl, jobName);
            }

        }

    }
}
