package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NearbyBusStationOfRealComplexRegisterRequestDto extends BaseDto {
    private Long realComplexId;
    private String nearBusStationId;
    private Long nearBusStationCost;
    private String nearBusStationMoveType;
}
