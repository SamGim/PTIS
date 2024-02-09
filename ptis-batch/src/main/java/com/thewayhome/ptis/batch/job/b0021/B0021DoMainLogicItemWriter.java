package com.thewayhome.ptis.batch.job.b0021;

import com.thewayhome.ptis.core.service.RealComplexService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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
@Qualifier("B0021DoMainLogicItemWriter")
@StepScope
public class B0021DoMainLogicItemWriter implements ItemWriter<B0021DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private final RealComplexService realComplexService;
    @PersistenceContext
    private final EntityManager entityManager;
    private StepExecution stepExecution;

    public B0021DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            RealComplexService realComplexService,
            EntityManager entityManager
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.realComplexService = realComplexService;
        this.entityManager = entityManager;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Transactional
    @Override
    public void write(Chunk<? extends B0021DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0021DoMainLogicItemOutput item : chunk.getItems()) {
            this.realComplexService.updateNearestNodeInfo(
                    item.getRealComplexId(),
                    item.getNearNodeId(),
                    item.getNearNodeCost()
            );
        }

        entityManager.flush();
        entityManager.clear();
        chunk.clear();
    }
}
