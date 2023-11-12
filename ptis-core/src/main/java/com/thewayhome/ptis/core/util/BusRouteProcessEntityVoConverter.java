package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.entity.BusRoute;
import com.thewayhome.ptis.core.entity.BusRouteProcess;
import com.thewayhome.ptis.core.repository.BusRouteProcessRepository;
import com.thewayhome.ptis.core.repository.BusRouteRepository;
import com.thewayhome.ptis.core.vo.BusRouteProcessVo;
import com.thewayhome.ptis.core.vo.BusRouteVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BusRouteProcessEntityVoConverter implements IEntityVoConverter<BusRouteProcess, BusRouteProcessVo> {
    private final BusRouteRepository busRouteRepository;
    private final BusRouteProcessRepository busRouteProcessRepository;
    private final BusRouteEntityVoConverter busRouteEntityVoConverter;

    @Override
    @NotNull
    public BusRouteProcess toEntity(BusRouteProcessVo vo, String operatorId) {
        BusRoute busRoute = busRouteRepository.findById(vo.getId()).orElseThrow(IllegalArgumentException::new);
        BusRouteProcess entity = busRouteProcessRepository.findById(vo.getId())
                .orElse(
                        BusRouteProcess.builder()
                                .id(vo.getId())
                                .busRoute(busRoute)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getBusRouteGatheringStatusCode() != null) entity.setBusRouteGatheringStatusCode(vo.getBusRouteGatheringStatusCode());
        if (vo.getBusRouteFirstGatheringDate() != null) entity.setBusRouteFirstGatheringDate(vo.getBusRouteFirstGatheringDate());
        if (vo.getBusRouteLastGatheringDate() != null) entity.setBusRouteLastGatheringDate(vo.getBusRouteLastGatheringDate());
        if (vo.getBusStationGatheringStatusCode() != null) entity.setBusStationGatheringStatusCode(vo.getBusStationGatheringStatusCode());
        if (vo.getBusStationFirstGatheringDate() != null) entity.setBusStationFirstGatheringDate(vo.getBusStationFirstGatheringDate());
        if (vo.getBusStationLastGatheringDate() != null) entity.setBusStationLastGatheringDate(vo.getBusStationLastGatheringDate());

        if (entity.getBusRouteFirstGatheringDate() == null && entity.getBusRouteLastGatheringDate() != null) {
            entity.setBusRouteFirstGatheringDate(entity.getBusRouteLastGatheringDate());
        }
        if (entity.getBusStationFirstGatheringDate() == null && entity.getBusStationLastGatheringDate() != null) {
            entity.setBusStationFirstGatheringDate(entity.getBusStationLastGatheringDate());
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public BusRouteProcessVo toVo(BusRouteProcess entity, String operatorId) {
        BusRouteVo busRouteVo = null;

        if (entity.getBusRoute() != null) busRouteVo = busRouteEntityVoConverter.toVo(entity.getBusRoute(), operatorId);

        return BusRouteProcessVo.builder()
                .id(entity.getId())
                .busRoute(busRouteVo)
                .busRouteGatheringStatusCode(entity.getBusRouteGatheringStatusCode())
                .busRouteFirstGatheringDate(entity.getBusRouteFirstGatheringDate())
                .busRouteLastGatheringDate(entity.getBusRouteLastGatheringDate())
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
