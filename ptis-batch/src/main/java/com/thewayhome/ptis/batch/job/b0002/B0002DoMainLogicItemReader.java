package com.thewayhome.ptis.batch.job.b0002;

import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.entity.BusStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("B0002DoMainLogicItemReader")
@StepScope
public class B0002DoMainLogicItemReader implements ItemReader<B0002DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final List<B0002DoMainLogicItemInput> items;
    private final BusStationService busStationService;

    public B0002DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            BusStationService busStationService
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.busStationService = busStationService;

        this.items = new ArrayList<>();
        initialize();
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    public void initialize() throws IndexOutOfBoundsException, JobInterruptedException {
        log.info("### B0002 Reader :: Initialize 시작 ###");

        log.info("### B0002 Reader :: B0002 Step 종료상태 검증 시작 ###");
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            log.warn("### B0002 Reader :: B0002 Step 종료상태 확인 ###");
            throw new JobInterruptedException("Job is stopping");
        }
        log.info("### B0002 Reader :: B0002 Step 종료상태 검증 완료 ###");

        log.info("### B0002 Reader :: 버스노선을 수집하지 않은 버스정류장 목록 발췌 시작 ###");
        List<BusStation> busStationList = busStationService.findBusStationByBusRouteGatheringStatusCode("00", false);
        log.info("### B0002 Reader :: 버스노선을 수집하지 않은 버스정류장 목록 발췌 완료 ###");

        log.info("### B0002 Reader :: 버스정류장 목록을 B0002DoMainLogicItemInput 객체로 변환 시작 ###");
        for (BusStation busStation : busStationList) {
            this.items.add(B0002DoMainLogicItemInput
                    .builder()
                    .id(busStation.getId())
                    .arsId(busStation.getBusStationNo())
                    .build()
            );
        }
        log.info("### B0002 Reader :: 버스정류장 목록을 B0002DoMainLogicItemInput 객체로 변환 완료 ###");

        log.info("### B0002 Reader :: Initialize 종료 ###");
    }

    @Override
    public B0002DoMainLogicItemInput read() {
        log.info("### B0002 Reader :: read 시작 ###");

        log.info("### B0002 Reader :: B0002 Step 종료상태 검증 ###");
        if (stepExecution.isTerminateOnly()) {
            log.warn("### B0002 Step 종료상태 확인 ###");
            return null;
        }
        log.info("### B0002 Reader :: B0002 Job 종료상태 검증 완료 ###");

        log.info("### B0002 Reader :: read 종료 ###");

        return items.isEmpty() ? null : items.remove(0);
    }
}
