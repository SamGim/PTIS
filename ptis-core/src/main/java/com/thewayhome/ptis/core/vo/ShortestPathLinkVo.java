package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ShortestPathLinkVo extends BaseVo {
    private String id;
    private NodeVo stNode;
    private NodeVo edNode;
    private NodeVo prevNode;
    private Long cost;
}
