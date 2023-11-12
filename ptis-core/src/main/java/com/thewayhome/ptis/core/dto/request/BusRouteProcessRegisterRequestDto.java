package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteProcessRegisterRequestDto extends BaseDto {
    private String id;
    private String gatheringStatusCode;
    private String selfGatheringStatusCode;
    private String stationGatheringStatusCode;
}
