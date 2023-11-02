package com.thewayhome.ptis.batch.job.b0005;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0005DoMainLogicItemInput {
    private String id;
    private String busRouteId;
}
