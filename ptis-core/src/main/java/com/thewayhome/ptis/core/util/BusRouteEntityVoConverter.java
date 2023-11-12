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
        BusStation startStation = busStationRepository.findById(vo.getBusStationSt().getId()).orElseThrow(IllegalArgumentException::new);
        BusStation endStation = busStationRepository.findById(vo.getBusStationEd().getId()).orElseThrow(IllegalArgumentException::new);

        BusRoute entity = busRouteRepository.findById(vo.getId())
                .or(() -> busRouteRepository.findByBusRouteId(vo.getBusRouteId()))
                .orElse(
                        BusRoute.builder()
                                .id(vo.getId())
                                .busRouteId(vo.getBusRouteId())
                                .busRouteName(vo.getBusRouteName())
                                .busRouteNo(vo.getBusRouteNo())
                                .busRouteSubNo(vo.getBusRouteSubNo())
                                .busStationSt(startStation)
                                .busStationEd(endStation)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        entity.setBusRouteId(vo.getBusRouteId() == null ? entity.getBusRouteId() : vo.getBusRouteId());
        entity.setBusRouteName(vo.getBusRouteName() == null ? entity.getBusRouteName() : vo.getBusRouteName());
        entity.setBusRouteNo(vo.getBusRouteNo() == null ? entity.getBusRouteNo() : vo.getBusRouteNo());
        entity.setBusRouteSubNo(vo.getBusRouteSubNo() == null ? entity.getBusRouteSubNo() : vo.getBusRouteSubNo());
        entity.setBusStationSt(startStation == null ? entity.getBusStationSt() : startStation);
        entity.setBusStationEd(endStation == null ? entity.getBusStationEd() : endStation);

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? operatorId : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? operatorId : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public BusRouteVo toVo(BusRoute entity, String operatorId) {
        BusStationVo startStation = busStationEntityVoConverter.toVo(entity.getBusStationSt(), operatorId);
        BusStationVo endStation = busStationEntityVoConverter.toVo(entity.getBusStationEd(), operatorId);

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
