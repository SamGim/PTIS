package com.thewayhome.ptis.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusRoute {
    private String busRouteId;
    private String busRouteNm;
    private int length;
    private int busRouteType;
    private String stBegin;
    private String stEnd;
}
