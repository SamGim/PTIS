package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import com.thewayhome.ptis.core.entity.BusStation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteRegisterReqDto extends BaseDto {
    private String busRouteId;
    private String busRouteName;
    private String busRouteNo;
    private String busRouteSubNo;
    private List<BusStation> stations;
}
