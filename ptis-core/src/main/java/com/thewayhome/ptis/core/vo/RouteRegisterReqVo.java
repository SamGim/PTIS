package com.thewayhome.ptis.core.vo;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RouteRegisterReqVo extends BaseRequestVo {
    private String stationStId;
    private String stationEdId;
    private long costBase;
    private long costSub;
    private long costAdd;
}
