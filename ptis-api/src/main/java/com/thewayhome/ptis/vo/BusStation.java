package com.thewayhome.ptis.vo;

import lombok.*;

@Data
@Builder
public class BusStation implements IStation{
    private String arsId;
    private String dist;
    private String gpsX;
    private String gpsY;
    private String posX;
    private String posY;
    private String stationId;
    private String stationNm;
    private String stationTp;

    @Override
    public String getStationId() {
        return this.getArsId();
    }

    @Override
    public String getStationName() {
        return this.getStationNm();
    }

    @Override
    public String getStationPosX() {
        return this.getGpsX();
    }

    @Override
    public String getStationPosY() {
        return this.getGpsY();
    }
}
