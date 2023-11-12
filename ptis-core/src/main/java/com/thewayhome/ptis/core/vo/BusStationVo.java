package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusStationVo extends BaseDto {
    private String id;
    private String busStationId;
    private String busStationName;
    private String busStationNo;
    private Double busStationPosX;
    private Double busStationPosY;
}
