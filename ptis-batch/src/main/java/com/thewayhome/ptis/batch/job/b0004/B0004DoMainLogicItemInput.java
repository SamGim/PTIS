package com.thewayhome.ptis.batch.job.b0004;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0004DoMainLogicItemInput {
    private String id;
    private String busRouteId;
}
