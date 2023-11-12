package com.thewayhome.ptis.batch.job.B0006;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0006DoMainLogicItemInput {
    private String nodeId;
    private String nodeName;
    private String nodePosX;
    private String nodePosY;
    private String nodeAddress;
}
