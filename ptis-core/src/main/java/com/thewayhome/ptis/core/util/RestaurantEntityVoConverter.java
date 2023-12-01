package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.RestaurantVo;
import com.thewayhome.ptis.core.entity.Restaurant;
import com.thewayhome.ptis.core.repository.RestaurantRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RestaurantEntityVoConverter implements IEntityVoConverter<Restaurant, RestaurantVo> {
    private final RestaurantRepository restaurantRepository;

    @Override
    @NotNull
    public Restaurant toEntity(RestaurantVo vo, String operatorId) {
        Restaurant entity = restaurantRepository.findById(vo.getId())
                .orElse(
                        Restaurant.builder()
                                .id(vo.getId())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getRestaurantId() != null) entity.setRestaurantId(vo.getRestaurantId());
        if (vo.getRestaurantName() != null) entity.setRestaurantName(vo.getRestaurantName());
        if (vo.getRestaurantType() != null) entity.setRestaurantType(vo.getRestaurantType());
        if (vo.getRestaurantAddress() != null) entity.setRestaurantAddress(vo.getRestaurantAddress());
        if (vo.getRestaurantPosX() != null) entity.setRestaurantPosX(vo.getRestaurantPosX());
        if (vo.getRestaurantPosY() != null) entity.setRestaurantPosY(vo.getRestaurantPosY());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public RestaurantVo toVo(Restaurant entity) {
        return RestaurantVo.builder()
                .id(entity.getId())
                .restaurantId(entity.getRestaurantId())
                .restaurantName(entity.getRestaurantName())
                .restaurantType(entity.getRestaurantType())
                .restaurantAddress(entity.getRestaurantAddress())
                .restaurantPosX(entity.getRestaurantPosX())
                .restaurantPosY(entity.getRestaurantPosY())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
