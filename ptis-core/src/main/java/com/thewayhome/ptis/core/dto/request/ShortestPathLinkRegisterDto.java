package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ShortestPathLinkRegisterDto extends BaseDto {
    private String id;
    private NodeVo stNode;
    private NodeVo edNode;
    private NodeVo prevNode;
    private Long cost;
}
