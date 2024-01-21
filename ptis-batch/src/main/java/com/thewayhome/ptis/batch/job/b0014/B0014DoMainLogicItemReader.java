package com.thewayhome.ptis.batch.job.b0014;

import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.NodeService;
import com.thewayhome.ptis.core.util.BusStationEntityVoConverter;
import com.thewayhome.ptis.core.vo.BusStationVo;
import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("B0014DoMainLogicItemReader")
@StepScope
public class B0014DoMainLogicItemReader implements ItemStreamReader<B0014DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final List<BusStationVo> items;
    private final NodeService nodeService;
    private final BusStationService busStationService;
    private final BusStationEntityVoConverter busStationEntityVoConverter;

    private int idx = 0;


    public B0014DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            NodeService nodeService,
            BusStationService busStationService,
            BusStationEntityVoConverter busStationEntityVoConverter
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.nodeService = nodeService;
        this.busStationService = busStationService;
        this.busStationEntityVoConverter = busStationEntityVoConverter;
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
        busStationService.findAll().forEach(busStation -> {
            if (nodeService.isExist(busStation.getId())) {
                items.add(busStationEntityVoConverter.toVo(busStation));
            }
        });

    }

    @Override
    public B0014DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }

        if (this.idx >= this.items.size()) {
            return null;
        }
        log.info("배치 14번 진행중 {}%", (double)(this.idx) / (double)(this.items.size()) * 100);
        return B0014DoMainLogicItemInput.builder()
                .targetNode(this.items.get(this.idx++))
                .build();
    }
}
