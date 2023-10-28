package com.thewayhome.ptis.batch.job.b0002;

import com.mysql.cj.util.StringUtils;
import com.thewayhome.ptis.batch.service.BatchJobService;
import com.thewayhome.ptis.batch.vo.BatchJob;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.vo.BusStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        List<BusStation> busStationList = busStationService.getAllBusStation();

        for (BusStation busStation : busStationList) {
            this.items.add(B0002DoMainLogicItemInput
                    .builder()
                    .arsId(busStation.getBusStationNo())
                    .build()
            );
        }
    }

    @Override
    public B0002DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }
        return items.isEmpty() ? null : items.remove(0);
    }
}
