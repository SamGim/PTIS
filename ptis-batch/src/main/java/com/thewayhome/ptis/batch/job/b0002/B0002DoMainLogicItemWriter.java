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
        log.info("### B0002 Writer :: write 시작 ###");

        log.info("### B0002 Writer :: B0002 Step 종료상태 검증 시작 ###");
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            log.warn("### B0002 Writer :: B0002 Step 종료상태 확인 ###");
            throw new JobInterruptedException("Job is stopping");
        }
        log.info("### B0002 Writer :: B0002 Step 종료상태 검증 완료 ###");

        log.info("### B0002 Writer :: Chunk 처리 시작 ###");
        for (B0002DoMainLogicItemOutput item : chunk.getItems()) {
            log.info("### B0002 Writer :: 버스정류장 상태코드 변경 시작 ###");
            busStationService.updateBusRoutesGatheringStatusCode(item.getBusStationProcessRegisterRequestDto());
            log.info("### B0002 Writer :: 버스정류장 상태코드 변경 완료 ###");

            log.info("### B0002 Writer :: 버스노선 목록 등록 처리 시작 ###");
            for (BusRouteRegisterRequestDto req : item.getBusRouteRegisterRequestDtoList()) {
                log.info("### B0002 Writer :: 버스노선 개별 등록 시작 ###");
                BusRouteVo busRouteVo = busRouteService.registerBusRoute(req);
                log.info("### B0002 Writer :: 버스노선 개별 등록 완료 ###");

                BusRouteProcessRegisterRequestDto prcReq = BusRouteProcessRegisterRequestDto.builder()
                                        .id(busRouteVo.getId())
                                        .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                                        .busRouteGatheringStatusCode("01")
                                        .operatorId(this.jobName)
                                .build();

                log.info("### B0002 Writer :: 버스노선 개별 처리결과 등록 시작 ###");
                busRouteService.registerBusRouteProcess(prcReq);
                log.info("### B0002 Writer :: 버스노선 개별 처리결과 등록 완료 ###");
            }
            log.info("### B0002 Writer :: 버스노선 목록 등록 처리 완료 ###");
        }


        log.info("### B0002 Writer :: write 완료 ###");
    }
}
