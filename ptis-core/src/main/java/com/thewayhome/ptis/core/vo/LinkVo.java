package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class LinkVo extends BaseVo {
    private String id;
    private String linkType;
    private String linkName;
    private NodeVo stNode;
    private NodeVo edNode;
    private Long cost;
}
