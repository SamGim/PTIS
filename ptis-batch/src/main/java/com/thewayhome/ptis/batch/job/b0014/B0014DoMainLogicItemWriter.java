package com.thewayhome.ptis.batch.job.b0014;

import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import com.thewayhome.ptis.core.service.BusRouteService;
import com.thewayhome.ptis.core.service.LinkService;
import com.thewayhome.ptis.core.service.MessageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

import java.util.List;

@Slf4j
@Component
@Qualifier("B0014DoMainLogicItemWriter")
@StepScope
public class B0014DoMainLogicItemWriter implements ItemWriter<B0014DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private final MessageService messageService;
    private final BusRouteService busRouteService;
    private final LinkService linkService;
    @PersistenceContext
    private final EntityManager entityManager;

    public B0014DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            MessageService messageService,
            BusRouteService busRouteService,
            LinkService linkService,
            EntityManager entityManager
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.messageService = messageService;
        this.busRouteService = busRouteService;
        this.linkService = linkService;
        this.entityManager = entityManager;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends B0014DoMainLogicItemOutput> chunk) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        for (B0014DoMainLogicItemOutput item : chunk.getItems()) {
            List<LinkRegisterRequestDto> linkRegisterReqDtoList = item.getLinkRegisterRequestDto();
            for (LinkRegisterRequestDto linkRegisterReqDto : linkRegisterReqDtoList) {
                linkService.registerLink(linkRegisterReqDto);
            }
        }

        entityManager.flush();
        entityManager.clear();
        chunk.clear();
    }
}
