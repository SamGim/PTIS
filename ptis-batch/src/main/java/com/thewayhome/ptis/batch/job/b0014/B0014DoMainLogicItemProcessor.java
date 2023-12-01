package com.thewayhome.ptis.batch.job.b0014;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.batch.util.GeoUtils;
import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import com.thewayhome.ptis.core.entity.BusRouteCourse;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.service.*;
import com.thewayhome.ptis.core.util.BusStationEntityVoConverter;
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
import java.time.LocalDateTime;
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
        NodeVo stNode = nodeService.findByBusStationId(stBusStation.getBusStationId(), jobName);
        // stNode를 포함하는 BusRouteCourse를 가져온다. 단 시간이 Null이면 안됨
        List<BusRouteCourse> busRouteCourseListIncludeStNode = busRouteCourseService.getBusRouteCourseByBusStationId(input.getTargetNode().getId());
        for (BusRouteCourse stBusRouteCourse : busRouteCourseListIncludeStNode) {
            // stNode를 포함한 BusRouteCourse와 동일한 BusRoute를 가지고있으면서, stBusRouteCourse의 firstBusTime보다 큰 BusRouteCourse를 가져온다. 단 당연히 시간이 Null이면 안됨
            List<BusRouteCourse> busRouteCourseListIncludeEdNode = busRouteCourseService.getBusRouteCourseByBusRouteIdAndTimeAfter(stBusRouteCourse.getBusRoute().getId());
            for (BusRouteCourse edBusRouteCourse : busRouteCourseListIncludeEdNode) {
                // edBusRouteCourse의 BusStation과 Node를 가져온다.
                BusStationVo curDestBusStation = busStationEntityVoConverter.toVo(edBusRouteCourse.getBusStation());
                NodeVo edNode = nodeService.findByBusStationId(curDestBusStation.getBusStationId(), jobName);
                LocalTime curDestBusTime = edBusRouteCourse.getFirstBusTime();
                // targetBusRouteCourse
                long diff = Duration.between(stBusRouteCourse.getFirstBusTime(), curDestBusTime).toNanos();
                // targetNode와 DestNode로 이루어진 Link를 찾아온다.

                LinkVo link = linkService.findBySourceNodeAndDestNode(stNode, edNode, "B", jobName);
                LinkRegisterRequestDto req = LinkRegisterRequestDto.builder()
                        .id(link.getId())
                        .stNode(link.getStNode())
                        .linkName(link.getLinkName())
                        .edNode(link.getEdNode())
                        .linkType(link.getLinkType())
                        .cost(link.getCost())
                        .operatorId(jobName)
                        .build();
                // diff값이 기존 Link의 cost보다 작으면 Link의 cost를 diff값으로 변경한다. 그리고 Link를 저장한다.
                if (link.getCost() > diff) {
                    req.setCost(diff);
                    linkRegisterReqDtoList.add(req);
                }

            }
        }
        return B0014DoMainLogicItemOutput.builder()
                .linkRegisterRequestDto(linkRegisterReqDtoList)
                .build();
    }

}
