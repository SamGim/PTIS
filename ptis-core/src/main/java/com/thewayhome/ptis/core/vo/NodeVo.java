package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NodeVo extends BaseVo {
    private String id;
    private String nodeName;
    private Double nodePosX;
    private Double nodePosY;
    private String nodeSrcType;
    private String nodeSrcId;
}
