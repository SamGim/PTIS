package com.thewayhome.ptis.batch.job.b0009;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0009DoMainLogicItemInput {
    private Long companyId;
    private String companyName;
    private Double companyPosX;
    private Double companyPosY;
}
