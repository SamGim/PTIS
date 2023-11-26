package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteCourseVo extends BaseDto {
    private String id;
    private BusRouteVo busRoute;
    private BusStationVo busStation;
    private LocalDateTime firstBusTime;
    private LocalDateTime lastBusTime;
}
