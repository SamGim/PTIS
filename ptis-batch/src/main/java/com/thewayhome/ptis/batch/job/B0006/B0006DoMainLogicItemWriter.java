package com.thewayhome.ptis.batch.job.B0006;


import com.thewayhome.ptis.core.dto.request.GymRegisterRequestDto;
import com.thewayhome.ptis.core.service.GymService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0006DoMainLogicItemWriter")
@StepScope
public class B0006DoMainLogicItemWriter implements ItemWriter<B0006DoMainLogicItemOutput> {

    private final String jobName;
    private final String jobDate;

    private final GymService gymService;

    public B0006DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            GymService gymService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.gymService = gymService;
    }

    @Override
    public void write(Chunk<? extends B0006DoMainLogicItemOutput> chunk) throws Exception {
        for (B0006DoMainLogicItemOutput item : chunk.getItems()) {
            GymRegisterRequestDto newEntity = GymRegisterRequestDto.builder()
                    .gymId(item.getNodeId())
                    .gymAddress(item.getNodeAddress())
                    .gymPosX(item.getNodePosX())
                    .gymPosY(item.getNodePosY())
                    .gymName(item.getNodeName())
                    .operatorId(this.jobName)
                    .build();

            // DB
            newEntity.setOperatorId(this.jobName);
            gymService.registerGym(newEntity);
        }

    }
}
