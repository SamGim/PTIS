package com.thewayhome.ptis.batch.job.b0008;

import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0008DoMainLogicItemOutput {
    private String complexId;
    private NodeRegisterRequestDto nodeRegisterReqDto;
}
