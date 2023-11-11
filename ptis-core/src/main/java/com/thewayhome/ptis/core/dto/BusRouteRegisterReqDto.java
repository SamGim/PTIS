package com.thewayhome.ptis.core.dto;

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
public class BusRouteRegisterReqDto extends BaseRequestDto {
    private String busRouteId;
    private String busRouteName;
    private String busRouteNo;
    private String busRouteSubNo;
    private List<BusStation> stations;
}