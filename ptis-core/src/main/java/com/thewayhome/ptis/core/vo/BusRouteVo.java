package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteVo extends BaseVo {
    private String id;
    private String busRouteId;
    private String busRouteName;
    private String busRouteNo;
    private String busRouteSubNo;
}
