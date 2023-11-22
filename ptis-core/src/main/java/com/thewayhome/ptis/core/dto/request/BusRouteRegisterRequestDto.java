package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.vo.BusStationVo;
import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusRouteRegisterRequestDto extends BaseDto {
    private String id;
    private String busRouteId;
    private String busRouteName;
    private String busRouteNo;
    private String busRouteSubNo;
    private BusStationVo busStationSt;
    private BusStationVo busStationEd;
}
