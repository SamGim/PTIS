package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamsRegisterReqDto extends BaseDto {
    private String groupName;
    private String paramName;
    private String value;
    private String useYn;
}
