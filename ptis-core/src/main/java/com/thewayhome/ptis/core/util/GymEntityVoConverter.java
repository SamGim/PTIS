package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.entity.Gym;
import com.thewayhome.ptis.core.repository.GymRepository;
import com.thewayhome.ptis.core.vo.GymVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GymEntityVoConverter implements IEntityVoConverter<Gym, GymVo> {
    private final GymRepository gymRepository;

    @Override
    @NotNull
    public Gym toEntity(GymVo vo, String operatorId) {
        Gym entity = gymRepository.findById(vo.getId())
                .orElse(
                        Gym.builder()
                                .id(vo.getId())
                                .gymId(vo.getGymId())
                                .gymName(vo.getGymName())
                                .gymAddress(vo.getGymAddress())
                                .gymPosX(vo.getGymPosX())
                                .gymPosY(vo.getGymPosY())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getGymId() != null) entity.setGymId(vo.getGymId());
        if (vo.getGymName() != null) entity.setGymName(vo.getGymName());
        if (vo.getGymAddress() != null) entity.setGymAddress(vo.getGymAddress());
        if (vo.getGymPosX() != null) entity.setGymPosX(vo.getGymPosX());
        if (vo.getGymPosY() != null) entity.setGymPosY(vo.getGymPosY());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public GymVo toVo(Gym entity, String operatorId) {
        return GymVo.builder()
                .id(entity.getId())
                .gymId(entity.getGymId())
                .gymName(entity.getGymName())
                .gymAddress(entity.getGymAddress())
                .gymPosX(entity.getGymPosX())
                .gymPosY(entity.getGymPosY())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
