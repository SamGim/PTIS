package com.thewayhome.ptis.batch.job.b0003;

import com.thewayhome.ptis.core.dto.request.BusRouteProcessRegisterReqDto;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterReqDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0003DoMainLogicItemOutput {
    private String id;
    private String arsId;
    private BusRouteProcessRegisterReqDto busRouteProcessRegisterReqVo;
    private List<BusStationRegisterReqDto> busStationRegisterReqVoList;
}
