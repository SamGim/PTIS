package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.BusStationVo;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.repository.BusStationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BusStationEntityVoConverter implements IEntityVoConverter<BusStation, BusStationVo> {
    private final BusStationRepository busStationRepository;

    @Override
    @NotNull
    public BusStation toEntity(BusStationVo vo, String operatorId) {
        BusStation entity = busStationRepository.findById(vo.getId())
                .orElse(
                        BusStation.builder()
                                .id(vo.getId())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getBusStationId() != null) entity.setBusStationId(vo.getBusStationId());
        if (vo.getBusStationName() != null) entity.setBusStationName(vo.getBusStationName());
        if (vo.getBusStationNo() != null) entity.setBusStationNo(vo.getBusStationNo());
        if (vo.getBusStationPosX() != null) entity.setBusStationPosX(vo.getBusStationPosX());
        if (vo.getBusStationPosY() != null) entity.setBusStationPosY(vo.getBusStationPosY());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public BusStationVo toVo(BusStation entity, String operatorId) {
        return BusStationVo.builder()
                .id(entity.getId())
                .busStationId(entity.getBusStationId())
                .busStationName(entity.getBusStationName())
                .busStationNo(entity.getBusStationNo())
                .busStationPosX(entity.getBusStationPosX())
                .busStationPosY(entity.getBusStationPosY())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
