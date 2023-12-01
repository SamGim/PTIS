package com.thewayhome.ptis.batch.job.b0003;

import com.thewayhome.ptis.core.service.BusRouteService;
import com.thewayhome.ptis.core.entity.BusRoute;
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
@Qualifier("B0003DoMainLogicItemReader")
@StepScope
public class B0003DoMainLogicItemReader implements ItemReader<B0003DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final List<B0003DoMainLogicItemInput> items;
    private final BusRouteService busRouteService;

    public B0003DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            BusRouteService busRouteService
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.busRouteService = busRouteService;

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

        List<BusRoute> busRouteList = busRouteService.findBusRouteByGatheringStatusCode("01", true);

        for (BusRoute busRoute : busRouteList) {
            this.items.add(B0003DoMainLogicItemInput
                    .builder()
                    .id(busRoute.getId())
                    .busRouteId(busRoute.getBusRouteId())
                    .build()
            );
        }
    }

    @Override
    public B0003DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }
        return items.isEmpty() ? null : items.remove(0);
    }
}
