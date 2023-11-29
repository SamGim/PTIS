package com.thewayhome.ptis.batch.job.b0014;

import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0014DoMainLogicItemOutput {
    private List<LinkRegisterRequestDto> linkRegisterRequestDto;
}
