package com.thewayhome.ptis.batch.job.b0009;

import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.Company;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("B0009DoMainLogicItemReader")
@StepScope
public class B0009DoMainLogicItemReader implements ItemStreamReader<B0009DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private int idx = 0;
    private final List<B0009DoMainLogicItemInput> items;
    private final CompanyService companyService;

    public B0009DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            CompanyService companyService
    ) throws IOException, IndexOutOfBoundsException, JobInterruptedException {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.companyService = companyService;
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

        List<Company> companyList = companyService.getAllCompany();

        for (Company company : companyList) {
            this.items.add(B0009DoMainLogicItemInput
                    .builder()
                    .companyId(company.getCompanyId())
                    .companyName(company.getCompanyName())
                    .companyPosX(company.getLongitude())
                    .companyPosY(company.getLatitude())
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
    public B0009DoMainLogicItemInput read() {
        if (stepExecution.isTerminateOnly()) {
            return null;
        }
        if (this.idx >= this.items.size()) {
            return null;
        }
        return this.items.get(this.idx++);
    }
}
