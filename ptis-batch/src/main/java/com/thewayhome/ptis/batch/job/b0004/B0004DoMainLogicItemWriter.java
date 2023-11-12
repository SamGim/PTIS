package com.thewayhome.ptis.batch.job.b0004;

import com.thewayhome.ptis.core.service.RestaurantService;
import com.thewayhome.ptis.core.dto.request.RestaurantRegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0004DoMainLogicItemWriter")
@StepScope
public class B0004DoMainLogicItemWriter implements ItemWriter<B0004DoMainLogicItemOutput> {

    private final String jobName;
    private final String jobDate;

    private final RestaurantService restaurantService;

    public B0004DoMainLogicItemWriter(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            RestaurantService restaurantService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.restaurantService = restaurantService;
    }

    @Override
    public void write(Chunk<? extends B0004DoMainLogicItemOutput> chunk) throws Exception {
        for (B0004DoMainLogicItemOutput item : chunk.getItems()) {
            RestaurantRegisterRequestDto newEntity = RestaurantRegisterRequestDto.builder()
                    .restaurantId(item.getNodeId())
                    .restaurantAddress(item.getNodeAddress())
                    .restaurantPosX(item.getNodePosX())
                    .restaurantPosY(item.getNodePosY())
                    .restaurantType(item.getNodeType())
                    .restaurantName(item.getNodeName())
                    .build();

            // DB
            newEntity.setOperatorId(this.jobName);
            restaurantService.registerRestaurant(newEntity);
        }

    }
}
