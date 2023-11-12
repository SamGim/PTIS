package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.BusStationVo;
import com.thewayhome.ptis.core.vo.BusStationProcessVo;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.BusStationProcess;
import com.thewayhome.ptis.core.repository.BusStationProcessRepository;
import com.thewayhome.ptis.core.repository.BusStationRepository;
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
        BusStation busStation = busStationRepository.findById(vo.getBusStation().getId()).orElseThrow(IllegalArgumentException::new);
        BusStationProcess entity = busStationProcessRepository.findById(vo.getId())
                .orElse(
                        BusStationProcess.builder()
                                .id(vo.getId())
                                .busStation(busStation)
                                .busStationGatheringStatusCode(vo.getBusStationGatheringStatusCode())
                                .busStationFirstGatheringDate(vo.getBusStationFirstGatheringDate())
                                .busStationLastGatheringDate(vo.getBusStationLastGatheringDate())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        entity.setBusStation(vo.getBusStation() == null ? entity.getBusStation() : busStation);
        entity.setBusStationGatheringStatusCode(vo.getBusStationGatheringStatusCode() == null ? entity.getBusStationGatheringStatusCode() : vo.getBusStationGatheringStatusCode());
        entity.setBusStationFirstGatheringDate(vo.getBusStationFirstGatheringDate() == null ? entity.getBusStationFirstGatheringDate() : vo.getBusStationFirstGatheringDate());
        entity.setBusStationLastGatheringDate(vo.getBusStationLastGatheringDate() == null ? entity.getBusStationLastGatheringDate() : vo.getBusStationLastGatheringDate());

        entity.setBusStationFirstGatheringDate(
                entity.getBusStationFirstGatheringDate() == null || entity.getBusStationFirstGatheringDate().isBlank() ?
                        vo.getBusStationLastGatheringDate() == null || vo.getBusStationLastGatheringDate().isBlank() ?
                                entity.getBusStationFirstGatheringDate() :
                                vo.getBusStationLastGatheringDate() :
                        entity.getBusStationFirstGatheringDate()
        );
        entity.setBusRouteFirstGatheringDate(
                entity.getBusRouteFirstGatheringDate() == null || entity.getBusRouteFirstGatheringDate().isBlank() ?
                        vo.getBusRouteLastGatheringDate() == null || vo.getBusRouteLastGatheringDate().isBlank() ?
                                entity.getBusRouteFirstGatheringDate() :
                                vo.getBusRouteLastGatheringDate() :
                        entity.getBusRouteFirstGatheringDate()
        );

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? operatorId : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? operatorId : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public BusStationProcessVo toVo(BusStationProcess entity, String operatorId) {
        BusStationVo busStationVo = busStationEntityVoConverter.toVo(entity.getBusStation(), operatorId);
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
