package com.thewayhome.ptis.batch.job.b0021;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0021DoMainLogicItemOutput {
    private Long realComplexId;
    private String nearNodeId;
    private Long nearNodeCost;
    private String nearNodeMoveType;
}
