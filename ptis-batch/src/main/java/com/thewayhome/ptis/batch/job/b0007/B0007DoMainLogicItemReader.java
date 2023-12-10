package com.thewayhome.ptis.batch.job.b0007;

import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.service.BusStationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("B0007DoMainLogicItemReader")
@StepScope
public class B0007DoMainLogicItemReader implements ItemReader<B0007DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final List<B0007DoMainLogicItemInput> items;
    private final BusStationService busStationService;

    public B0007DoMainLogicItemReader(
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
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        List<BusStation> busStationList = busStationService.findBusStationByNodeCreationStatusCode("00", false);

        for (BusStation busStation : busStationList) {
            this.items.add(B0007DoMainLogicItemInput
                    .builder()
                    .busStationId(busStation.getId())
                    .busStationName(busStation.getBusStationName())
                    .busStationPosX(busStation.getBusStationPosX())
                    .busStationPosY(busStation.getBusStationPosY())
                    .build()
            );
        }
    }

    @Override
    public B0007DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }
        return items.isEmpty() ? null : items.remove(0);
    }
}
