package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ParamsRegisterRequestDto extends BaseDto {
    private String groupName;
    private String paramName;
    private String value;
    private String useYn;
}
