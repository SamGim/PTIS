package com.thewayhome.ptis.batch.job.b0010;

import com.thewayhome.ptis.core.dto.LinkRegisterReqDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0010DoMainLogicItemOutput {
    private LinkRegisterReqDto linkRegisterReqDto;
}
