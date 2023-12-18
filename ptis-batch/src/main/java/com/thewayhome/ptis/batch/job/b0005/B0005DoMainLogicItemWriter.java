package com.thewayhome.ptis.batch.job.b0005;

import com.thewayhome.ptis.core.dto.request.BusStationProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.response.BusStationRegisterResponseDto;
import com.thewayhome.ptis.core.service.BusRouteService;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.MessageService;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterRequestDto;
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
@Qualifier("B0005DoMainLogicItemWriter")
@StepScope
public class B0005DoMainLogicItemWriter implements ItemWriter<B0005DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final MessageService messageService;
    private final BusRouteService busRouteService;
    private final BusStationService busStationService;

    public B0005DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            MessageService messageService,
            BusRouteService busRouteService,
            BusStationService busStationService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.messageService = messageService;
        this.busRouteService = busRouteService;
        this.busStationService = busStationService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends B0005DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0005DoMainLogicItemOutput item : chunk.getItems()) {
            busRouteService.updateBusStationsGatheringStatusCode(item.getBusRouteProcessRegisterRequestDto());

            for (BusStationRegisterRequestDto req : item.getBusStationRegisterRequestDtoList()) {
                BusStationRegisterResponseDto busStationRegisterResponseDto = busStationService.registerBusStation(req);
                BusStationVo busStationVo = busStationRegisterResponseDto.getBusStationVo();

                BusStationProcessRegisterRequestDto prcReq = BusStationProcessRegisterRequestDto.builder()
                        .id(busStationVo.getId())
                        .busStationLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .busStationGatheringStatusCode("01")
                        .busRouteLastGatheringDate(" ")
                        .busRouteGatheringStatusCode("00")
                        .nodeLastCreationDate(" ")
                        .nodeCreationStatusCode("00")
                        .operatorId(this.jobName)
                        .build();

                busStationService.registerBusStationProcess(prcReq);
            }
        }
    }
}
