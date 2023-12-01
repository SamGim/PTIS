package com.thewayhome.ptis.batch.job.b0020;

import com.thewayhome.ptis.core.dto.request.ShortestPathLinkRegisterDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0020DoMainLogicItemOutput {
    private List<ShortestPathLinkRegisterDto> shortestPathLinkRegisterDtoList;
}
