package com.thewayhome.ptis.batch.job.b0007;

import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0007DoMainLogicItemOutput {
    private String busStationId;
    private NodeRegisterRequestDto nodeRegisterReqDto;
}
