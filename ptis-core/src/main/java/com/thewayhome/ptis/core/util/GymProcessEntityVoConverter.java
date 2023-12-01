package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.entity.Gym;
import com.thewayhome.ptis.core.entity.GymProcess;
import com.thewayhome.ptis.core.repository.GymProcessRepository;
import com.thewayhome.ptis.core.repository.GymRepository;
import com.thewayhome.ptis.core.vo.GymProcessVo;
import com.thewayhome.ptis.core.vo.GymVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GymProcessEntityVoConverter implements IEntityVoConverter<GymProcess, GymProcessVo> {
    private final GymRepository gymRepository;
    private final GymProcessRepository gymProcessRepository;
    private final GymEntityVoConverter gymEntityVoConverter;

    @Override
    @NotNull
    public GymProcess toEntity(GymProcessVo vo, String operatorId) {
        Gym gym = gymRepository.findById(vo.getId()).orElseThrow(IllegalArgumentException::new);
        GymProcess entity = gymProcessRepository.findById(vo.getId())
                .orElse(
                        GymProcess.builder()
                                .id(vo.getId())
                                .gym(gym)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getGymGatheringStatusCode() != null) entity.setGymGatheringStatusCode(vo.getGymGatheringStatusCode());
        if (vo.getGymFirstGatheringDate() != null) entity.setGymFirstGatheringDate(vo.getGymFirstGatheringDate());
        if (vo.getGymLastGatheringDate() != null) entity.setGymLastGatheringDate(vo.getGymLastGatheringDate());
        if (vo.getNodeCreationStatusCode() != null) entity.setNodeCreationStatusCode(vo.getNodeCreationStatusCode());
        if (vo.getNodeFirstCreationDate() != null) entity.setNodeFirstCreationDate(vo.getNodeFirstCreationDate());
        if (vo.getNodeLastCreationDate() != null) entity.setNodeLastCreationDate(vo.getNodeLastCreationDate());

        if (entity.getGymGatheringStatusCode() == null) {
            entity.setGymGatheringStatusCode("00");
        }
        if (entity.getGymFirstGatheringDate() == null) {
            entity.setGymFirstGatheringDate(" ");
        }
        if (entity.getGymLastGatheringDate() == null) {
            entity.setGymLastGatheringDate(" ");
        }
        if (entity.getNodeCreationStatusCode() == null) {
            entity.setNodeCreationStatusCode("00");
        }
        if (entity.getNodeFirstCreationDate() == null) {
            entity.setNodeFirstCreationDate(" ");
        }
        if (entity.getNodeLastCreationDate() == null) {
            entity.setNodeLastCreationDate(" ");
        }

        if (entity.getGymFirstGatheringDate() == null && vo.getGymLastGatheringDate() != null) {
            entity.setGymFirstGatheringDate(vo.getGymLastGatheringDate());
        }
        if (entity.getNodeFirstCreationDate() == null && vo.getNodeLastCreationDate() != null) {
            entity.setNodeFirstCreationDate(vo.getNodeLastCreationDate());
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public GymProcessVo toVo(GymProcess entity, String operatorId) {
        GymVo gymVo = null;

        if (entity.getGym() != null) gymVo = gymEntityVoConverter.toVo(entity.getGym(), operatorId);

        return GymProcessVo.builder()
                .id(entity.getId())
                .gym(gymVo)
                .gymGatheringStatusCode(entity.getGymGatheringStatusCode())
                .gymFirstGatheringDate(entity.getGymFirstGatheringDate())
                .gymLastGatheringDate(entity.getGymLastGatheringDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
