package com.thewayhome.ptis.batch.vo;

import com.thewayhome.ptis.core.vo.BaseRequestVo;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchJobRegisterReqVo extends BaseRequestVo {
    private String name;
    private String inputType;
    private String inputFilename;
    private String inputDelimiter;
    private String useYn;
}
