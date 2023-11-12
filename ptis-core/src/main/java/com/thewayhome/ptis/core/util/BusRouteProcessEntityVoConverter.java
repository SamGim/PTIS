package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.BusRouteVo;
import com.thewayhome.ptis.core.vo.BusRouteProcessVo;
import com.thewayhome.ptis.core.entity.BusRoute;
import com.thewayhome.ptis.core.entity.BusRouteProcess;
import com.thewayhome.ptis.core.repository.BusRouteProcessRepository;
import com.thewayhome.ptis.core.repository.BusRouteRepository;
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
        BusRoute busRoute = busRouteRepository.findById(vo.getBusRoute().getId()).orElseThrow(IllegalArgumentException::new);
        BusRouteProcess entity = busRouteProcessRepository.findById(vo.getId())
                .orElse(
                        BusRouteProcess.builder()
                                .id(vo.getId())
                                .busRoute(busRoute)
                                .busRouteGatheringStatusCode(vo.getBusRouteGatheringStatusCode())
                                .busRouteFirstGatheringDate(vo.getBusRouteFirstGatheringDate())
                                .busRouteLastGatheringDate(vo.getBusRouteLastGatheringDate())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        entity.setBusRoute(vo.getBusRoute() == null ? entity.getBusRoute() : busRoute);
        entity.setBusRouteGatheringStatusCode(vo.getBusRouteGatheringStatusCode() == null ? entity.getBusRouteGatheringStatusCode() : vo.getBusRouteGatheringStatusCode());
        entity.setBusRouteFirstGatheringDate(
                vo.getBusRouteFirstGatheringDate() == null || vo.getBusRouteFirstGatheringDate().isBlank() ?
                        entity.getBusRouteFirstGatheringDate() :
                        vo.getBusRouteFirstGatheringDate()
        );
        entity.setBusRouteLastGatheringDate(
                vo.getBusRouteLastGatheringDate() == null || vo.getBusRouteLastGatheringDate().isBlank() ?
                        entity.getBusRouteLastGatheringDate() :
                        vo.getBusRouteLastGatheringDate()
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
    public BusRouteProcessVo toVo(BusRouteProcess entity, String operatorId) {
        BusRouteVo busRouteVo = busRouteEntityVoConverter.toVo(entity.getBusRoute(), operatorId);
        return BusRouteProcessVo.builder()
                .id(entity.getId())
                .busRoute(busRouteVo)
                .busRouteGatheringStatusCode(entity.getBusRouteGatheringStatusCode())
                .busRouteFirstGatheringDate(entity.getBusRouteFirstGatheringDate())
                .busRouteLastGatheringDate(entity.getBusRouteLastGatheringDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
