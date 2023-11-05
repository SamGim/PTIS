package com.thewayhome.ptis.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RouteRegisterReqDto extends BaseRequestDto {
    private String stationStId;
    private String stationEdId;
    private long costBase;
    private long costSub;
    private long costAdd;
}
