package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import com.thewayhome.ptis.core.vo.BusRouteVo;
import com.thewayhome.ptis.core.vo.BusStationVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteAddCourseItemRequestDto extends BaseDto {
    private String id;
    private BusRouteVo busRoute;
    private BusStationVo busStation;
    private LocalTime firstBusTime;
    private LocalTime lastBusTime;
}
