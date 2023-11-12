package com.thewayhome.ptis.batch.job.b0002;

import com.thewayhome.ptis.core.dto.request.BusRouteRegisterReqDto;
import com.thewayhome.ptis.core.dto.request.BusStationProcessRegisterReqDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0002DoMainLogicItemOutput {
    private String id;
    private String arsId;
    private BusStationProcessRegisterReqDto busStationProcessRegisterReqVo;
    private List<BusRouteRegisterReqDto> busRouteRegisterReqVoList;
}
