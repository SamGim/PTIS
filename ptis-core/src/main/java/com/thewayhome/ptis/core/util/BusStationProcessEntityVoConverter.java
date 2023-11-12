package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.BusStationProcess;
import com.thewayhome.ptis.core.repository.BusStationProcessRepository;
import com.thewayhome.ptis.core.repository.BusStationRepository;
import com.thewayhome.ptis.core.vo.BusStationProcessVo;
import com.thewayhome.ptis.core.vo.BusStationVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BusStationProcessEntityVoConverter implements IEntityVoConverter<BusStationProcess, BusStationProcessVo> {
    private final BusStationRepository busStationRepository;
    private final BusStationProcessRepository busStationProcessRepository;
    private final BusStationEntityVoConverter busStationEntityVoConverter;

    @Override
    @NotNull
    public BusStationProcess toEntity(BusStationProcessVo vo, String operatorId) {
        BusStation busStation = busStationRepository.findById(vo.getId()).orElseThrow(IllegalArgumentException::new);
        BusStationProcess entity = busStationProcessRepository.findById(vo.getId())
                .orElse(
                        BusStationProcess.builder()
                                .id(vo.getId())
                                .busStation(busStation)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getBusStationGatheringStatusCode() != null) entity.setBusStationGatheringStatusCode(vo.getBusStationGatheringStatusCode());
        if (vo.getBusStationFirstGatheringDate() != null) entity.setBusStationFirstGatheringDate(vo.getBusStationFirstGatheringDate());
        if (vo.getBusStationLastGatheringDate() != null) entity.setBusStationLastGatheringDate(vo.getBusStationLastGatheringDate());
        if (vo.getBusRouteGatheringStatusCode() != null) entity.setBusRouteGatheringStatusCode(vo.getBusRouteGatheringStatusCode());
        if (vo.getBusRouteFirstGatheringDate() != null) entity.setBusRouteFirstGatheringDate(vo.getBusRouteFirstGatheringDate());
        if (vo.getBusRouteLastGatheringDate() != null) entity.setBusRouteLastGatheringDate(vo.getBusRouteLastGatheringDate());
        if (vo.getNodeCreationStatusCode() != null) entity.setNodeCreationStatusCode(vo.getNodeCreationStatusCode());
        if (vo.getNodeFirstCreationDate() != null) entity.setNodeFirstCreationDate(vo.getNodeFirstCreationDate());
        if (vo.getNodeLastCreationDate() != null) entity.setNodeLastCreationDate(vo.getNodeLastCreationDate());

        if (entity.getBusRouteFirstGatheringDate() == null && entity.getBusRouteLastGatheringDate() != null) {
            entity.setBusRouteFirstGatheringDate(entity.getBusRouteLastGatheringDate());
        }
        if (entity.getBusStationFirstGatheringDate() == null && entity.getBusStationLastGatheringDate() != null) {
            entity.setBusStationFirstGatheringDate(entity.getBusStationLastGatheringDate());
        }
        if (entity.getNodeFirstCreationDate() == null && entity.getNodeLastCreationDate() != null) {
            entity.setNodeFirstCreationDate(entity.getNodeLastCreationDate());
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public BusStationProcessVo toVo(BusStationProcess entity, String operatorId) {
        BusStationVo busStationVo = null;

        if (entity.getBusStation() != null) busStationVo = busStationEntityVoConverter.toVo(entity.getBusStation(), operatorId);

        return BusStationProcessVo.builder()
                .id(entity.getId())
                .busStation(busStationVo)
                .busStationGatheringStatusCode(entity.getBusStationGatheringStatusCode())
                .busStationFirstGatheringDate(entity.getBusStationFirstGatheringDate())
                .busStationLastGatheringDate(entity.getBusStationLastGatheringDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
