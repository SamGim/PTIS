package com.thewayhome.ptis.batch.job.b0010;

import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0010DoMainLogicItemInput {
    private NodeVo srcNode;
    private NodeVo destNode;
}
