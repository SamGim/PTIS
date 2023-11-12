package com.thewayhome.ptis.batch.job.b0002;

import com.thewayhome.ptis.core.dto.request.BusRouteProcessRegisterRequestDto;
import com.thewayhome.ptis.core.service.BusRouteService;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.MessageService;
import com.thewayhome.ptis.core.dto.request.BusRouteRegisterRequestDto;
import com.thewayhome.ptis.core.vo.BusRouteVo;
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
@Qualifier("B0002DoMainLogicItemWriter")
@StepScope
public class B0002DoMainLogicItemWriter implements ItemWriter<B0002DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final MessageService messageService;
    private final BusRouteService busRouteService;
    private final BusStationService busStationService;

    public B0002DoMainLogicItemWriter(
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
    public void write(Chunk<? extends B0002DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0002DoMainLogicItemOutput item : chunk.getItems()) {
            busStationService.updateBusRoutesGatheringStatusCode(item.getBusStationProcessRegisterRequestDto());

            for (BusRouteRegisterRequestDto req : item.getBusRouteRegisterRequestDtoList()) {
                BusRouteVo busRouteVo = busRouteService.registerBusRoute(req);

                BusRouteProcessRegisterRequestDto prcReq = BusRouteProcessRegisterRequestDto.builder()
                                        .id(busRouteVo.getId())
                                        .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                                        .busRouteGatheringStatusCode("01")
                                        .busStationLastGatheringDate(" ")
                                        .busStationGatheringStatusCode("00")
                                        .operatorId(this.jobName)
                                .build();

                busRouteService.registerBusRouteProcess(prcReq);
            }
        }
    }
}
