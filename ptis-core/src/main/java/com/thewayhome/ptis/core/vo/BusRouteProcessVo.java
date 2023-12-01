package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteProcessVo extends BaseVo {
    private String id;
    private BusRouteVo busRoute;
    private String busRouteGatheringStatusCode;
    private String busRouteFirstGatheringDate;
    private String busRouteLastGatheringDate;
    private String busStationGatheringStatusCode;
    private String busStationFirstGatheringDate;
    private String busStationLastGatheringDate;
}
