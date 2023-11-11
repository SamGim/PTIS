package com.thewayhome.ptis.batch.job.b0010;

import com.thewayhome.ptis.core.entity.Node;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0010DoMainLogicItemInput {
    private Node srcNode;
    private Node destNode;
}
