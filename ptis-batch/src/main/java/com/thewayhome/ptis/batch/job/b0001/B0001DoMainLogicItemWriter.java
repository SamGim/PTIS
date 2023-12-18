package com.thewayhome.ptis.batch.job.b0001;

import com.thewayhome.ptis.core.dto.request.BusStationProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterRequestDto;
import com.thewayhome.ptis.core.dto.response.BusStationRegisterResponseDto;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.vo.BusStationVo;
import lombok.extern.slf4j.Slf4j;
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
@Qualifier("B0001DoMainLogicItemWriter")
@StepScope
public class B0001DoMainLogicItemWriter implements ItemWriter<B0001DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private final BusStationService busStationService;

    public B0001DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            BusStationService busStationService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.busStationService = busStationService;
    }

    @Override
    public void write(Chunk<? extends B0001DoMainLogicItemOutput> chunk) throws Exception {
        for (B0001DoMainLogicItemOutput item : chunk.getItems()) {
            BusStationRegisterRequestDto req = BusStationRegisterRequestDto.builder()
                    .busStationId(item.getNodeId())
                    .busStationNo(item.getArsId())
                    .busStationName(item.getNodeName())
                    .busStationPosX(item.getNodePosX())
                    .busStationPosY(item.getNodePosY())
                    .operatorId(this.jobName)
                    .build();

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
