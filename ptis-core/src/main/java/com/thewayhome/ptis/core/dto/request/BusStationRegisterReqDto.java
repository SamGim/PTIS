package com.thewayhome.ptis.core.dto.request;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusStationRegisterReqDto extends BaseRequestDto {
    private String busStationId;
    private String busStationName;
    private String busStationNo;
    private String busStationPosX;
    private String busStationPosY;
}
