package com.thewayhome.ptis.batch.job.b0007;

import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import com.thewayhome.ptis.core.service.BusRouteService;
import com.thewayhome.ptis.core.service.MessageService;
import com.thewayhome.ptis.core.service.NodeService;
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
@Qualifier("B0007DoMainLogicItemWriter")
@StepScope
public class B0007DoMainLogicItemWriter implements ItemWriter<B0007DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final MessageService messageService;
    private final BusRouteService busRouteService;
    private final NodeService nodeService;

    public B0007DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            MessageService messageService,
            BusRouteService busRouteService,
            NodeService nodeService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.messageService = messageService;
        this.busRouteService = busRouteService;
        this.nodeService = nodeService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends B0007DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0007DoMainLogicItemOutput item : chunk.getItems()) {
            NodeRegisterRequestDto nodeRegisterReqDto = item.getNodeRegisterReqDto();
            String busStationId = item.getBusStationId();

            nodeService.createNodeFromBusStation(nodeRegisterReqDto, busStationId);

            nodeRegisterReqDto = null;
            busStationId = null;
        }

        chunk.clear();
    }
}
