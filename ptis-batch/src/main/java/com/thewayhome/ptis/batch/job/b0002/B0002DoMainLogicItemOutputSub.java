package com.thewayhome.ptis.batch.job.b0002;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0002DoMainLogicItemOutputSub {
    private String busRouteId;
    private String busRouteNm;
    private String busRouteNo;
    private String busRouteSubNo;
}