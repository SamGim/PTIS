package com.thewayhome.ptis.batch.job.b0020;

import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0020DoMainLogicItemInput {
    private NodeVo iNode;
    private NodeVo kNode;
}
