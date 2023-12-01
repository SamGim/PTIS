package com.thewayhome.ptis.batch.job.b0009;

import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0009DoMainLogicItemOutput {
    private Long companyId;
    private NodeRegisterRequestDto nodeRegisterReqDto;
}
