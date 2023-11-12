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
        Restaurant restaurant = restaurantRepository.findById(vo.getRestaurant().getId()).orElseThrow(IllegalArgumentException::new);
        RestaurantProcess entity = restaurantProcessRepository.findById(vo.getId())
                .orElse(
                        RestaurantProcess.builder()
                                .id(vo.getId())
                                .restaurant(restaurant)
                                .gatheringStatusCode(vo.getGatheringStatusCode())
                                .firstGatheringDate(vo.getFirstGatheringDate())
                                .lastGatheringDate(vo.getLastGatheringDate())
                                .createdAt(LocalDateTime.now())
                                .createdBy(vo.getOperatorId())
                                .build()
                );

        entity.setRestaurant(vo.getRestaurant() == null ? entity.getRestaurant() : restaurant);
        entity.setGatheringStatusCode(vo.getGatheringStatusCode() == null ? entity.getGatheringStatusCode() : vo.getGatheringStatusCode());
        entity.setFirstGatheringDate(vo.getFirstGatheringDate() == null ? entity.getFirstGatheringDate() : vo.getFirstGatheringDate());
        entity.setLastGatheringDate(vo.getLastGatheringDate() == null ? entity.getLastGatheringDate() : vo.getLastGatheringDate());

        entity.setFirstGatheringDate(
                entity.getFirstGatheringDate() == null || entity.getFirstGatheringDate().isBlank() ?
                        vo.getLastGatheringDate() == null || vo.getLastGatheringDate().isBlank() ?
                                entity.getFirstGatheringDate() :
                                vo.getLastGatheringDate() :
                        entity.getFirstGatheringDate()
        );

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? vo.getOperatorId() : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? vo.getOperatorId() : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public RestaurantProcessVo toVo(RestaurantProcess entity, String operatorId) {
        RestaurantVo restaurant = restaurantEntityVoConverter.toVo(entity.getRestaurant(), operatorId);
        return RestaurantProcessVo.builder()
                .id(entity.getId())
                .restaurant(restaurant)
                .gatheringStatusCode(entity.getGatheringStatusCode())
                .firstGatheringDate(entity.getFirstGatheringDate())
                .lastGatheringDate(entity.getLastGatheringDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
