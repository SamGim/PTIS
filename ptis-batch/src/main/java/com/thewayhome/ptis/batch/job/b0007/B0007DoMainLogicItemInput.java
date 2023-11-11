package com.thewayhome.ptis.batch.job.b0007;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0007DoMainLogicItemInput {
    private String busStationId;
    private String busStationName;
    private double busStationPosX;
    private double busStationPosY;
}
