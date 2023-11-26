package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteVo extends BaseDto {
    private String id;
    private String busRouteId;
    private String busRouteName;
    private String busRouteNo;
    private String busRouteSubNo;
}
