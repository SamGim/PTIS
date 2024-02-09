package com.thewayhome.ptis.batch.job.b0021;

import com.thewayhome.ptis.core.entity.complex.RealComplex;
import com.thewayhome.ptis.core.service.RealComplexService;
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
@Qualifier("B0021DoMainLogicItemReader")
@StepScope
public class B0021DoMainLogicItemReader implements ItemReader<B0021DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private final String stInqDt;
    private final String edInqDt;
    private final String walkSpeed;
    private final List<B0021DoMainLogicItemInput> items;
    private final RealComplexService realComplexService;
    private StepExecution stepExecution;

    public B0021DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            @Value("#{jobParameters[stInqDt]}") String stInqDt,
            @Value("#{jobParameters[edInqDt]}") String edInqDt,
            @Value("#{jobParameters[walkSpeed]}") String walkSpeed,
            RealComplexService realComplexService
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.stInqDt = stInqDt == null ? "00000101" : stInqDt;
        this.edInqDt = edInqDt == null ? "99991231" : edInqDt;
        this.walkSpeed = walkSpeed == null ? "4" : walkSpeed;
        this.realComplexService = realComplexService;

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

        List<RealComplex> realComplexList = realComplexService.findAll();

        for (RealComplex realComplex : realComplexList) {
            this.items.add(B0021DoMainLogicItemInput
                    .builder()
                    .readComplexId(realComplex.getId())
                    .build()
            );
        }
    }

    @Override
    public B0021DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }
        return items.isEmpty() ? null : items.remove(0);
    }
}
