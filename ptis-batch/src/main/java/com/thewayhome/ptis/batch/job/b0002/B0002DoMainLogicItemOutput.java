package com.thewayhome.ptis.batch.job.b0002;

import com.thewayhome.ptis.core.vo.BusRouteRegisterReqVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0002DoMainLogicItemOutput {
    private String arsId;
    private List<BusRouteRegisterReqVo> busRouteRegisterReqVoList;
}
