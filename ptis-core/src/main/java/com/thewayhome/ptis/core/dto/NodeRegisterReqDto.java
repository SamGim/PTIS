package com.thewayhome.ptis.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeRegisterReqDto extends BaseRequestDto {
    private String nodeName;
    private double nodePosX;
    private double nodePosY;
}
