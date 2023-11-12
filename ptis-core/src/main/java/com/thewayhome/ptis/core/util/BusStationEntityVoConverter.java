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
                                .busStationId(vo.getBusStationId())
                                .busStationName(vo.getBusStationName())
                                .busStationNo(vo.getBusStationNo())
                                .busStationPosX(vo.getBusStationPosX())
                                .busStationPosY(vo.getBusStationPosY())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        entity.setBusStationId(vo.getBusStationId() == null ? entity.getBusStationId() : vo.getBusStationId());
        entity.setBusStationName(vo.getBusStationName() == null ? entity.getBusStationName() : vo.getBusStationName());
        entity.setBusStationNo(vo.getBusStationNo() == null ? entity.getBusStationNo() : vo.getBusStationNo());
        entity.setBusStationPosX(vo.getBusStationPosX() == null ? entity.getBusStationPosX() : vo.getBusStationPosX());
        entity.setBusStationPosY(vo.getBusStationPosY() == null ? entity.getBusStationPosY() : vo.getBusStationPosY());

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? operatorId : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? operatorId : entity.getUpdatedBy());

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
