package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import com.thewayhome.ptis.core.entity.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusStationProcessRegisterRequestDto extends BaseDto {
    private String id;
    private String busStationGatheringStatusCode;
    private String busStationFirstGatheringDate;
    private String busStationLastGatheringDate;
    private String busRouteGatheringStatusCode;
    private String busRouteFirstGatheringDate;
    private String busRouteLastGatheringDate;
    private String nodeCreationStatusCode;
    private String nodeFirstCreationDate;
    private String nodeLastCreationDate;
    private Node node;
}
