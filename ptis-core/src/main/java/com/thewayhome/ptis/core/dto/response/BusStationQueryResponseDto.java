package com.thewayhome.ptis.core.dto.response;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusStationQueryResponseDto extends BaseDto {
    private String arsId;
    private String dist;
    private String gpsX;
    private String gpsY;
    private String posX;
    private String posY;
    private String stationId;
    private String stationNm;
    private String stationTp;
    private String busStationId;
}
