package com.thewayhome.ptis.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusSttnInfoInqireServiceRequestVo {
    private String serviceKey;
    private int pageNo;
    private int numOfRows;
    private String _type;
    private Double gpsLati;
    private Double gpsLong;
}
