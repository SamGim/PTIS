package com.thewayhome.ptis.batch.job.b0004;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0004DoMainLogicItemOutput {
    private String nodeId;
    private String nodeName;
    private String nodePosX;
    private String nodePosY;
    private String nodeAddress;
    private String nodeType;
}
