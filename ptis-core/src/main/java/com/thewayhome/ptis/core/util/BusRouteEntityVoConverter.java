package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.BusRouteVo;
import com.thewayhome.ptis.core.vo.BusStationVo;
import com.thewayhome.ptis.core.entity.BusRoute;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.repository.BusRouteRepository;
import com.thewayhome.ptis.core.repository.BusStationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BusRouteEntityVoConverter implements IEntityVoConverter<BusRoute, BusRouteVo> {
    private final BusRouteRepository busRouteRepository;
    private final BusStationRepository busStationRepository;
    private final BusStationEntityVoConverter busStationEntityVoConverter;

    @Override
    @NotNull
    public BusRoute toEntity(BusRouteVo vo, String operatorId) {
        BusStation startStation = null;
        BusStation endStation = null;

        if (vo.getBusStationSt() != null) startStation = busStationRepository.findById(vo.getBusStationSt().getId()).orElseThrow(IllegalArgumentException::new);
        if (vo.getBusStationEd() != null) endStation = busStationRepository.findById(vo.getBusStationEd().getId()).orElseThrow(IllegalArgumentException::new);

        BusRoute entity = busRouteRepository.findById(vo.getId())
                .or(() -> busRouteRepository.findByBusRouteId(vo.getBusRouteId()))
                .orElse(
                        BusRoute.builder()
                                .id(vo.getId())
                                .busStationSt(startStation)
                                .busStationEd(endStation)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getBusRouteId() != null) entity.setBusRouteId(vo.getBusRouteId());
        if (vo.getBusRouteName() != null) entity.setBusRouteName(vo.getBusRouteName());
        if (vo.getBusRouteNo() != null) entity.setBusRouteNo(vo.getBusRouteNo());
        if (vo.getBusRouteSubNo() != null) entity.setBusRouteSubNo(vo.getBusRouteSubNo());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public BusRouteVo toVo(BusRoute entity, String operatorId) {
        BusStationVo startStation = null;
        BusStationVo endStation = null;

        if (entity.getBusStationSt() != null) startStation = busStationEntityVoConverter.toVo(entity.getBusStationSt(), operatorId);
        if (entity.getBusStationEd() != null) endStation = busStationEntityVoConverter.toVo(entity.getBusStationEd(), operatorId);

        return BusRouteVo.builder()
                .id(entity.getId())
                .busRouteId(entity.getBusRouteId())
                .busRouteName(entity.getBusRouteName())
                .busRouteNo(entity.getBusRouteNo())
                .busRouteSubNo(entity.getBusRouteSubNo())
                .busStationSt(startStation)
                .busStationEd(endStation)
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
