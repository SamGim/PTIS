package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusStationProcessVo extends BaseDto {
    private String id;
    private BusStationVo busStation;
    private String busStationGatheringStatusCode;
    private String busStationFirstGatheringDate;
    private String busStationLastGatheringDate;
    private String busRouteGatheringStatusCode;
    private String busRouteFirstGatheringDate;
    private String busRouteLastGatheringDate;
    private String nodeCreationStatusCode;
    private String nodeFirstCreationDate;
    private String nodeLastCreationDate;
    private NodeVo node;
}
