package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteProcessRegisterRequestDto extends BaseDto {
    private String id;
    private String busRouteGatheringStatusCode;
    private String busRouteFirstGatheringDate;
    private String busRouteLastGatheringDate;
    private String busStationGatheringStatusCode;
    private String busStationFirstGatheringDate;
    private String busStationLastGatheringDate;
}
