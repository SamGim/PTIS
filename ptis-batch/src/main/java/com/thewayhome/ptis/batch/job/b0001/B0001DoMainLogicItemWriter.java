package com.thewayhome.ptis.batch.job.b0001;

import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.dto.BusStationRegisterReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
            BusStationRegisterReqDto newEntity = BusStationRegisterReqDto.builder()
                    .busStationId(item.getNodeId())
                    .busStationNo(item.getArsId())
                    .busStationName(item.getNodeName())
                    .busStationPosX(item.getNodePosX())
                    .busStationPosY(item.getNodePosY())
                    .build();

            // DB
            newEntity.setOperatorId(this.jobName);

            busStationService.saveBusStation(newEntity);
        }
    }
}
