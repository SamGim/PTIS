package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteCourseVo extends BaseVo {
    private String id;
    private BusRouteVo busRoute;
    private BusStationVo busStation;
    private LocalTime firstBusTime;
    private LocalTime lastBusTime;
}
