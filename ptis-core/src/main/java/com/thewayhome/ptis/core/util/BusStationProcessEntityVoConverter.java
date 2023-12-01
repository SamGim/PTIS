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

        if (entity.getBusRouteGatheringStatusCode() == null) {
            entity.setBusRouteFirstGatheringDate("00");
        }
        if (entity.getBusRouteFirstGatheringDate() == null) {
            entity.setBusRouteFirstGatheringDate(" ");
        }
        if (entity.getBusRouteLastGatheringDate() == null) {
            entity.setBusRouteLastGatheringDate(" ");
        }
        if (entity.getBusStationGatheringStatusCode() == null) {
            entity.setBusStationGatheringStatusCode("00");
        }
        if (entity.getBusStationFirstGatheringDate() == null) {
            entity.setBusStationFirstGatheringDate(" ");
        }
        if (entity.getBusStationLastGatheringDate() == null) {
            entity.setBusStationLastGatheringDate(" ");
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

        if (entity.getBusRouteFirstGatheringDate() == null && vo.getBusRouteLastGatheringDate() != null) {
            entity.setBusRouteFirstGatheringDate(vo.getBusRouteLastGatheringDate());
        }
        if (entity.getBusStationFirstGatheringDate() == null && vo.getBusStationLastGatheringDate() != null) {
            entity.setBusStationFirstGatheringDate(vo.getBusStationLastGatheringDate());
        }
        if (entity.getNodeFirstCreationDate() == null && vo.getNodeLastCreationDate() != null) {
            entity.setNodeFirstCreationDate(vo.getNodeLastCreationDate());
        }
        if (entity.getBusRouteFirstGatheringDate() != null && entity.getBusRouteFirstGatheringDate().isBlank() && vo.getBusRouteLastGatheringDate() != null) {
            entity.setBusRouteFirstGatheringDate(vo.getBusRouteLastGatheringDate());
        }
        if (entity.getBusStationFirstGatheringDate() != null && entity.getBusStationFirstGatheringDate().isBlank() && vo.getBusStationLastGatheringDate() != null) {
            entity.setBusStationFirstGatheringDate(vo.getBusStationLastGatheringDate());
        }
        if (entity.getNodeFirstCreationDate() != null && entity.getNodeFirstCreationDate().isBlank() && vo.getNodeLastCreationDate() != null) {
            entity.setNodeFirstCreationDate(vo.getNodeLastCreationDate());
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public BusStationProcessVo toVo(BusStationProcess entity) {
        BusStationVo busStationVo = null;

        if (entity.getBusStation() != null) busStationVo = busStationEntityVoConverter.toVo(entity.getBusStation());

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
                .build();
    }
}
