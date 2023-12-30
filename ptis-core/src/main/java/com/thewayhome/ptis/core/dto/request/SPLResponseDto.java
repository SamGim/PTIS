package com.thewayhome.ptis.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SPLResponseDto {
    private String stNodeName;
    private String stNodeSrcType;
    private String edNodeName;
    private String edNodeSrcType;
    private String cost;
    private String linkName;
    private String linkType;
}
