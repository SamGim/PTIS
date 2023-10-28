package com.thewayhome.ptis.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StationRegisterReqVo extends BaseRequestVo {
    private String stationId;
    private String stationPosX;
    private String stationPosY;
}
