package com.thewayhome.ptis.batch.job.b0008;

import com.thewayhome.ptis.core.entity.RealComplex;
import com.thewayhome.ptis.core.service.RealComplexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("B0008DoMainLogicItemReader")
@StepScope
public class B0008DoMainLogicItemReader implements ItemStreamReader<B0008DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private int idx = 0;
    private final List<B0008DoMainLogicItemInput> items;
    private final RealComplexService complexService;

    public B0008DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            RealComplexService complexService
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.complexService = complexService;
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

        List<RealComplex> complexList = complexService.findAll();

        for (RealComplex complex : complexList) {
            this.items.add(B0008DoMainLogicItemInput
                    .builder()
                    .complexId(String.format("%012d", complex.getId()))
                    .complexName(complex.getName())
                    .complexPosX(complex.getLongitude())
                    .complexPosY(complex.getLatitude())
                    .build()
            );
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.idx = executionContext.getInt("READ_IDX", 0);
        log.info("READ_IDX: " + this.idx);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put("READ_IDX", this.idx);
        log.info("UPDATE_IDX : " + this.idx);
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("CLOSE_IDX : " + this.idx);
    }
    @Override
    public B0008DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }
        if (this.idx >= this.items.size()) {
            return null;
        }
        return this.items.get(this.idx++);
    }
}
