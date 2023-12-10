package com.thewayhome.ptis.batch.job.b0003;

import com.thewayhome.ptis.core.dto.request.BusRouteAddCourseItemRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterRequestDto;
import com.thewayhome.ptis.core.service.BusRouteCourseService;
import com.thewayhome.ptis.core.service.BusRouteService;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.MessageService;
import com.thewayhome.ptis.core.vo.BusStationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@Qualifier("B0003DoMainLogicItemWriter")
@StepScope
public class B0003DoMainLogicItemWriter implements ItemWriter<B0003DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final MessageService messageService;
    private final BusRouteService busRouteService;
    private final BusStationService busStationService;
    private final BusRouteCourseService busRouteCourseService;

    public B0003DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            MessageService messageService,
            BusRouteService busRouteService,
            BusStationService busStationService,
            BusRouteCourseService busRouteCourseService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.messageService = messageService;
        this.busRouteService = busRouteService;
        this.busStationService = busStationService;
        this.busRouteCourseService = busRouteCourseService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends B0003DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0003DoMainLogicItemOutput item : chunk.getItems()) {
            busRouteService.updateBusStationsGatheringStatusCode(item.getBusRouteProcessRegisterRequestDto());

            for (BusStationRegisterRequestDto req : item.getBusStationRegisterRequestDtoList()) {
                BusStationVo busStationVo = busStationService.registerBusStation(req);

                BusStationProcessRegisterRequestDto prcReq = BusStationProcessRegisterRequestDto.builder()
                        .id(busStationVo.getId())
                        .busStationLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .busStationGatheringStatusCode("03")
                        .busRouteLastGatheringDate(" ")
                        .busRouteGatheringStatusCode("00")
                        .operatorId(this.jobName)
                        .build();

                busStationService.registerBusStationProcess(prcReq);
            }

            for (BusRouteAddCourseItemRequestDto req : item.getBusRouteAddCourseItemRequestDtoList()) {
                busRouteCourseService.registerBusRouteCourse(req);
            }
        }
    }
}
