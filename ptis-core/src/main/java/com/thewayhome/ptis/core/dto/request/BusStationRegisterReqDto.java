package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusStationRegisterReqDto extends BaseDto {
    private String busStationId;
    private String busStationName;
    private String busStationNo;
    private String busStationPosX;
    private String busStationPosY;
}
