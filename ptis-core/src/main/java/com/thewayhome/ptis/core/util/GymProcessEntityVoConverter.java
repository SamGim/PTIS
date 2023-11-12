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
        Gym gym = gymRepository.findById(vo.getGym().getId()).orElseThrow(IllegalArgumentException::new);
        GymProcess entity = gymProcessRepository.findById(vo.getId())
                .orElse(
                        GymProcess.builder()
                                .id(vo.getId())
                                .gym(gym)
                                .gymGatheringStatusCode(vo.getGymGatheringStatusCode())
                                .gymFirstGatheringDate(vo.getGymFirstGatheringDate())
                                .gymLastGatheringDate(vo.getGymLastGatheringDate())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        entity.setGym(vo.getGym() == null ? entity.getGym() : gym);
        entity.setGymGatheringStatusCode(vo.getGymGatheringStatusCode() == null ? entity.getGymGatheringStatusCode() : vo.getGymGatheringStatusCode());
        entity.setGymFirstGatheringDate(vo.getGymFirstGatheringDate() == null ? entity.getGymFirstGatheringDate() : vo.getGymFirstGatheringDate());
        entity.setGymLastGatheringDate(vo.getGymLastGatheringDate() == null ? entity.getGymLastGatheringDate() : vo.getGymLastGatheringDate());

        entity.setGymFirstGatheringDate(
                entity.getGymFirstGatheringDate() == null || entity.getGymFirstGatheringDate().isBlank() ?
                        vo.getGymLastGatheringDate() == null || vo.getGymLastGatheringDate().isBlank() ?
                                entity.getGymFirstGatheringDate() :
                                vo.getGymLastGatheringDate() :
                        entity.getGymFirstGatheringDate()
        );
        entity.setNodeFirstCreationDate(
                entity.getNodeFirstCreationDate() == null || entity.getNodeFirstCreationDate().isBlank() ?
                        vo.getNodeLastCreationDate() == null || vo.getNodeLastCreationDate().isBlank() ?
                                entity.getNodeFirstCreationDate() :
                                vo.getNodeLastCreationDate() :
                        entity.getNodeFirstCreationDate()
        );

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? operatorId : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? operatorId : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public GymProcessVo toVo(GymProcess entity, String operatorId) {
        GymVo gymVo = gymEntityVoConverter.toVo(entity.getGym(), operatorId);
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
