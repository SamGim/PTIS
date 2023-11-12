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
                                .restaurantId(vo.getRestaurantId())
                                .restaurantName(vo.getRestaurantName())
                                .restaurantType(vo.getRestaurantType())
                                .restaurantAddress(vo.getRestaurantAddress())
                                .restaurantPosX(vo.getRestaurantPosX())
                                .restaurantPosY(vo.getRestaurantPosY())
                                .createdAt(LocalDateTime.now())
                                .createdBy(vo.getOperatorId())
                                .build()
                );


        entity.setRestaurantId(vo.getRestaurantId() == null ? entity.getRestaurantId() : vo.getRestaurantId());
        entity.setRestaurantName(vo.getRestaurantName() == null ? entity.getRestaurantName() : vo.getRestaurantName());
        entity.setRestaurantType(vo.getRestaurantType() == null ? entity.getRestaurantType() : vo.getRestaurantType());
        entity.setRestaurantAddress(vo.getRestaurantAddress() == null ? entity.getRestaurantAddress() : vo.getRestaurantAddress());
        entity.setRestaurantPosX(vo.getRestaurantPosX() == null ? entity.getRestaurantPosX() : vo.getRestaurantPosX());
        entity.setRestaurantPosY(vo.getRestaurantPosY() == null ? entity.getRestaurantPosY() : vo.getRestaurantPosY());

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? vo.getOperatorId() : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? vo.getOperatorId() : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public RestaurantVo toVo(Restaurant entity, String operatorId) {
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
                .operatorId(operatorId)
                .build();
    }
}
