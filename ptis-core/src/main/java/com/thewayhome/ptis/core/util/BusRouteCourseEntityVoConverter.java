package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.entity.BusRoute;
import com.thewayhome.ptis.core.entity.BusRouteCourse;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.repository.BusRouteCourseRepository;
import com.thewayhome.ptis.core.repository.BusRouteRepository;
import com.thewayhome.ptis.core.repository.BusStationRepository;
import com.thewayhome.ptis.core.vo.BusRouteCourseVo;
import com.thewayhome.ptis.core.vo.BusRouteVo;
import com.thewayhome.ptis.core.vo.BusStationVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BusRouteCourseEntityVoConverter implements IEntityVoConverter<BusRouteCourse, BusRouteCourseVo> {
    private final BusRouteRepository busRouteRepository;
    private final BusStationRepository busStationRepository;
    private final BusRouteCourseRepository busRouteCourseRepository;
    private final BusRouteEntityVoConverter busRouteEntityVoConverter;
    private final BusStationEntityVoConverter busStationEntityVoConverter;

    @Override
    @NotNull
    public BusRouteCourse toEntity(BusRouteCourseVo vo, String operatorId) {
        BusRoute route = null;
        BusStation station = null;

        if (vo.getBusRoute() != null) route = busRouteRepository.findByBusRouteId(vo.getBusRoute().getId()).orElseThrow(IllegalArgumentException::new);
        if (vo.getBusStation() != null) station = busStationRepository.findByBusStationId(vo.getBusStation().getId()).orElseThrow(IllegalArgumentException::new);

        BusRouteCourse entity = busRouteCourseRepository.findById(vo.getId())
                .orElse(
                        BusRouteCourse.builder()
                                .id(vo.getId())
                                .busRoute(route)
                                .busStation(station)
                                .firstBusTime(vo.getFirstBusTime())
                                .lastBusTime(vo.getLastBusTime())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getFirstBusTime() != null) entity.setFirstBusTime(vo.getFirstBusTime());
        if (vo.getLastBusTime() != null) entity.setLastBusTime(vo.getLastBusTime());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public BusRouteCourseVo toVo(BusRouteCourse entity, String operatorId) {
        BusRouteVo routeVo = null;
        BusStationVo stationVo = null;

        if (entity.getBusRoute() != null) routeVo = busRouteEntityVoConverter.toVo(entity.getBusRoute(), operatorId);
        if (entity.getBusStation() != null) stationVo = busStationEntityVoConverter.toVo(entity.getBusStation(), operatorId);

        return BusRouteCourseVo.builder()
                .id(entity.getId())
                .busRoute(routeVo)
                .busStation(stationVo)
                .firstBusTime(entity.getFirstBusTime())
                .lastBusTime(entity.getLastBusTime())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
