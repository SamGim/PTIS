package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusStationRegisterRequestDto extends BaseDto {
    private String id;
    private String busStationId;
    private String busStationName;
    private String busStationNo;
    private String busStationPosX;
    private String busStationPosY;
}
