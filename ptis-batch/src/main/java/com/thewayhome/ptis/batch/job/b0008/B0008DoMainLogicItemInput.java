package com.thewayhome.ptis.batch.job.b0008;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0008DoMainLogicItemInput {
    private Long complexId;
    private String complexName;
    private Double complexPosX;
    private Double complexPosY;
}
