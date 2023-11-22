package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.RestaurantVo;
import com.thewayhome.ptis.core.vo.RestaurantProcessVo;
import com.thewayhome.ptis.core.entity.Restaurant;
import com.thewayhome.ptis.core.entity.RestaurantProcess;
import com.thewayhome.ptis.core.repository.RestaurantProcessRepository;
import com.thewayhome.ptis.core.repository.RestaurantRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RestaurantProcessEntityVoConverter implements IEntityVoConverter<RestaurantProcess, RestaurantProcessVo> {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantProcessRepository restaurantProcessRepository;
    private final RestaurantEntityVoConverter restaurantEntityVoConverter;

    @Override
    @NotNull
    public RestaurantProcess toEntity(RestaurantProcessVo vo, String operatorId) {
        Restaurant restaurant = restaurantRepository.findById(vo.getId()).orElseThrow(IllegalArgumentException::new);
        RestaurantProcess entity = restaurantProcessRepository.findById(vo.getId())
                .orElse(
                        RestaurantProcess.builder()
                                .id(vo.getId())
                                .restaurant(restaurant)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getRestaurantGatheringStatusCode() != null) entity.setRestaurantGatheringStatusCode(vo.getRestaurantGatheringStatusCode());
        if (vo.getRestaurantFirstGatheringDate() != null) entity.setRestaurantFirstGatheringDate(vo.getRestaurantFirstGatheringDate());
        if (vo.getRestaurantLastGatheringDate() != null) entity.setRestaurantLastGatheringDate(vo.getRestaurantLastGatheringDate());

        if (entity.getRestaurantFirstGatheringDate() == null && entity.getRestaurantLastGatheringDate() != null) {
            entity.setRestaurantFirstGatheringDate(entity.getRestaurantLastGatheringDate());
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public RestaurantProcessVo toVo(RestaurantProcess entity, String operatorId) {
        RestaurantVo restaurant = null;

        if (entity.getRestaurant() != null) restaurant = restaurantEntityVoConverter.toVo(entity.getRestaurant(), operatorId);

        return RestaurantProcessVo.builder()
                .id(entity.getId())
                .restaurant(restaurant)
                .restaurantGatheringStatusCode(entity.getRestaurantGatheringStatusCode())
                .restaurantFirstGatheringDate(entity.getRestaurantFirstGatheringDate())
                .restaurantLastGatheringDate(entity.getRestaurantLastGatheringDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
