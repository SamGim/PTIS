package com.thewayhome.ptis.batch.job.b0014;

import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import com.thewayhome.ptis.core.service.*;
import com.thewayhome.ptis.core.util.BusStationEntityVoConverter;
import com.thewayhome.ptis.core.vo.BusRouteCourseVo;
import com.thewayhome.ptis.core.vo.BusStationVo;
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

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("B0014DoMainLogicItemProcessor")
@StepScope
public class B0014DoMainLogicItemProcessor implements ItemProcessor<B0014DoMainLogicItemInput, B0014DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private BusRouteCourseService busRouteCourseService;
    private BusStationEntityVoConverter busStationEntityVoConverter;
    private LinkService linkService;
    private NodeService nodeService;

    public B0014DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            BusRouteCourseService busRouteCourseService,
            LinkService linkService,
            BusStationEntityVoConverter busStationEntityVoConverter,
            NodeService nodeService

    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.busRouteCourseService = busRouteCourseService;
        this.linkService = linkService;
        this.busStationEntityVoConverter = busStationEntityVoConverter;
        this.nodeService = nodeService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
    @Override
    public B0014DoMainLogicItemOutput process(B0014DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }
        List<LinkRegisterRequestDto> linkRegisterReqDtoList = new ArrayList<>();
        // targetNode = stNode로 시작점이 되는 BusStation과 노드를 먼저 가져온다.
        BusStationVo stBusStation = input.getTargetNode();
        NodeVo stNode = nodeService.findByBusStationId(stBusStation.getId(), jobName);
        // stNode를 포함하는 BusRouteCourse를 가져온다. 단 시간이 Null이면 안됨
        List<BusRouteCourseVo> busRouteCourseListIncludeStNode = busRouteCourseService.getBusRouteCourseByBusStationId(input.getTargetNode().getId());
        for (BusRouteCourseVo stBusRouteCourse : busRouteCourseListIncludeStNode) {
            log.info("stBusStation : {}, routeId : {}", stBusStation.getBusStationName(), stBusRouteCourse.getBusRoute().getBusRouteName());
            // stNode를 포함한 BusRouteCourse와 동일한 BusRoute를 가지고있으면서, stBusRouteCourse의 firstBusTime보다 큰 BusRouteCourse를 가져온다. 단 당연히 시간이 Null이면 안됨
            List<BusRouteCourseVo> busRouteCourseListIncludeEdNode = busRouteCourseService.getBusRouteCourseByBusRouteIdAndTimeAfter(stBusRouteCourse);
            String routeName = stBusRouteCourse.getBusRoute().getBusRouteName();
            for (BusRouteCourseVo edBusRouteCourse : busRouteCourseListIncludeEdNode) {
                // edBusRouteCourse의 BusStation과 Node를 가져온다.
                BusStationVo curDestBusStation = edBusRouteCourse.getBusStation();
                NodeVo edNode = nodeService.findByBusStationId(curDestBusStation.getId(), jobName);
                LocalTime curDestBusTime = edBusRouteCourse.getFirstBusTime();
                // targetBusRouteCourse
                long diff = Duration.between(stBusRouteCourse.getFirstBusTime(), curDestBusTime).toMinutes();
                // targetNode와 DestNode로 이루어진 Link를 찾아온다.
                String linkName = String.format("%s-%s:%s", stNode.getNodeName(), edNode.getNodeName(), routeName);
                Optional<LinkVo> link = linkService.findByStNodeAndEdNodeAndLinkTypeAndLinkName(stNode, edNode, "B", linkName, jobName);

                LinkRegisterRequestDto req = LinkRegisterRequestDto.builder()
                        .stNode(stNode)
                        .linkName(linkName)
                        .edNode(edNode)
                        .linkType("B")
                        .cost(Long.MAX_VALUE)
                        .operatorId(jobName)
                        .build();

                if (link.isPresent()) {
                    req.setId(link.get().getId());
                    req.setCost(link.get().getCost());
                }
                log.info("stNode : {}, edNode : {}, oldcost : {}, newcost : {}", stNode.getNodeName(), edNode.getNodeName(), req.getCost(), diff);
                // diff값이 기존 Link의 cost보다 작으면 Link의 cost를 diff값으로 변경한다. 그리고 Link를 저장한다.
                if (req.getCost() > diff) {
                    req.setCost(diff);
                    req.setLinkName(linkName);
                    linkRegisterReqDtoList.add(req);
                }

            }
        }
        return B0014DoMainLogicItemOutput.builder()
                .linkRegisterRequestDto(linkRegisterReqDtoList)
                .build();
    }

}
