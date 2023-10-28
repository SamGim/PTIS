package com.thewayhome.ptis.batch.job.b0002;

import com.thewayhome.ptis.batch.util.APIConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("B0002DoMainLogicItemProcessor")
@StepScope
public class B0002DoMainLogicItemProcessor implements ItemProcessor<B0002DoMainLogicItemInput, B0002DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    public B0002DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
    @Override
    public B0002DoMainLogicItemOutput process(B0002DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        final String arsId = input.getArsId();
        final List<B0002DoMainLogicItemOutputSub> busRouteList = new ArrayList<>();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("arsId", arsId);

        String dataFromAPI = APIConnector.getDataFromAPI(
                "http://localhost:8082",
                "/ws-pure/getRouteByStationList",
                queryParams
        ).block();

        if (dataFromAPI == null) {
            throw new IllegalArgumentException();
        }

        return B0002DoMainLogicItemOutput.builder()
                .arsId(arsId)
                .message(dataFromAPI)
                //.busRouteList(busRouteList)
                .build();
    }
}
