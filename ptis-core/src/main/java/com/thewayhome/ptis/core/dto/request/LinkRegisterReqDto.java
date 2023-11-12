package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import com.thewayhome.ptis.core.entity.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkRegisterReqDto extends BaseDto {
    private String linkType;
    private String linkName;
    private Node stNode;
    private Node edNode;
    private long cost;
}
