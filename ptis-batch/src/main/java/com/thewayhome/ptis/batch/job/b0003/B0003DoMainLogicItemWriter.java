package com.thewayhome.ptis.batch.job.b0003;

import com.thewayhome.ptis.core.dto.request.BusRouteAddCourseItemRequestDto;
import com.thewayhome.ptis.core.service.BusRouteCourseService;
import com.thewayhome.ptis.core.service.BusRouteService;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.MessageService;
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

import java.util.HashMap;

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

//            for (BusStationRegisterRequestDto req : item.getBusStationRegisterRequestDtoList()) {
//                BusStationRegisterResponseDto busStationRegisterResponseDto = busStationService.registerBusStation(req);
//                BusStationVo busStationVo = busStationRegisterResponseDto.getBusStationVo();
//                Boolean isRegistered = busStationRegisterResponseDto.getIsRegistered();
//
//                BusStationProcessRegisterRequestDto prcReq = BusStationProcessRegisterRequestDto.builder()
//                        .id(busStationVo.getId())
//                        .busStationLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
//                        .busStationGatheringStatusCode("03")
//                        .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
//                        .busRouteGatheringStatusCode(isRegistered ? "01" : "00")
//                        .operatorId(this.jobName)
//                        .build();
//
//                busStationService.registerBusStationProcess(prcReq);
//            }

            HashMap<String, Integer> dupMap = new HashMap<>();

            for (BusRouteAddCourseItemRequestDto req : item.getBusRouteAddCourseItemRequestDtoList()) {
                if (!dupMap.containsKey(req.getBusStation().getBusStationId()+req.getLastBusTime())) {
                    dupMap.put(req.getBusStation().getBusStationId()+req.getLastBusTime(), 1);

//                    boolean isExists = busRouteCourseService.existsByBusRouteIdAndBusStationIdAndLastBusTime(
//                            req.getBusRoute().getId(),
//                            req.getBusStation().getId(),
//                            req.getLastBusTime()
//                    );
//
//                    if (!isExists) {
                        busRouteCourseService.registerBusRouteCourse(req);
//                    }
                }

            }
        }
    }
}
