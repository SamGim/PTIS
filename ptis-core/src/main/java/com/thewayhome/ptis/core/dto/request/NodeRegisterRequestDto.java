package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NodeRegisterRequestDto extends BaseDto {
    private String id;
    private String nodeName;
    private Double nodePosX;
    private Double nodePosY;
}
