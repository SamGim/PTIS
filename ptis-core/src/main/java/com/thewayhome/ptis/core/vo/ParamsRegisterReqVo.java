package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.BaseRequestVo;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamsRegisterReqVo extends BaseRequestVo {
    private String groupName;
    private String paramName;
    private String value;
    private String useYn;
}
