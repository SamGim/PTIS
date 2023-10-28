package com.thewayhome.ptis.batch.job.b0001;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0001DoMainLogicItemOutput {
    private String nodeId;
    private String arsId;
    private String nodeName;
    private String nodePosX;
    private String nodePosY;
    private String nodePosType;
}
