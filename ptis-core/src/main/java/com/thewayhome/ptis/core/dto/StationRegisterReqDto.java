package com.thewayhome.ptis.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StationRegisterReqDto extends BaseRequestDto {
    private String stationId;
    private String stationPosX;
    private String stationPosY;
}
