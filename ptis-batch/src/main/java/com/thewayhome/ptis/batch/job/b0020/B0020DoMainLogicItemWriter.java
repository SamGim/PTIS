package com.thewayhome.ptis.batch.job.b0020;

import com.thewayhome.ptis.core.service.BusRouteService;
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

@Slf4j
@Component
@Qualifier("B0020DoMainLogicItemWriter")
@StepScope
public class B0020DoMainLogicItemWriter implements ItemWriter<B0020DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final MessageService messageService;
    private final BusRouteService busRouteService;
//    private final ShortestPathLinkService shortestPathLinkService;

    public B0020DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            MessageService messageService,
            BusRouteService busRouteService
//            ShortestPathLinkService shortestPathLinkService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.messageService = messageService;
        this.busRouteService = busRouteService;
//        this.shortestPathLinkService = shortestPathLinkService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends B0020DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0020DoMainLogicItemOutput item : chunk.getItems()) {
//            item.getShortestPathLinkRegisterDtoList().forEach(shortestPathLinkService::registerShortestPathLink);
        }
    }
}
