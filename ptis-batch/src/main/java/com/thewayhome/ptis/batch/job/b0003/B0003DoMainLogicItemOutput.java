package com.thewayhome.ptis.batch.job.b0003;

import com.thewayhome.ptis.core.vo.BusRouteProcessRegisterReqVo;
import com.thewayhome.ptis.core.vo.BusStationRegisterReqVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0003DoMainLogicItemOutput {
    private String id;
    private String arsId;
    private BusRouteProcessRegisterReqVo busRouteProcessRegisterReqVo;
    private List<BusStationRegisterReqVo> busStationRegisterReqVoList;
}
