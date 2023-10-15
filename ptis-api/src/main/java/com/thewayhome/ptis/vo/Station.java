package com.thewayhome.ptis.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Station {
    private String arsId;
    private String dist;
    private String gpsX;
    private String gpsY;
    private String posX;
    private String posY;
    private String stationId;
    private String stationNm;
    private String stationTp;
}
