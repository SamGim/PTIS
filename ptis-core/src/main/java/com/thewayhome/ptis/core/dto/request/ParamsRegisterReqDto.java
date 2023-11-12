package com.thewayhome.ptis.core.dto.request;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamsRegisterReqDto extends BaseRequestDto {
    private String groupName;
    private String paramName;
    private String value;
    private String useYn;
}
