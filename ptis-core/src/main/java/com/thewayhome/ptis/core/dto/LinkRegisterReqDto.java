package com.thewayhome.ptis.core.dto;

import com.thewayhome.ptis.core.entity.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkRegisterReqDto extends BaseRequestDto {
    private String linkType;
    private String linkName;
    private Node stNode;
    private Node edNode;
    private long cost;
}
