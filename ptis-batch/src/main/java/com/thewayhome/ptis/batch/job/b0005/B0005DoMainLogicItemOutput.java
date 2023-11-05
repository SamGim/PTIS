package com.thewayhome.ptis.batch.job.b0005;

import com.thewayhome.ptis.core.dto.BusRouteProcessRegisterReqDto;
import com.thewayhome.ptis.core.dto.BusStationRegisterReqDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0005DoMainLogicItemOutput {
    private String id;
    private String arsId;
    private BusRouteProcessRegisterReqDto busRouteProcessRegisterReqVo;
    private List<BusStationRegisterReqDto> busStationRegisterReqVoList;
}
