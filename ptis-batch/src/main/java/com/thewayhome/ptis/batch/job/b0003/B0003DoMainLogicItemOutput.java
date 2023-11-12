package com.thewayhome.ptis.batch.job.b0003;

import com.thewayhome.ptis.core.dto.request.BusRouteProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterRequestDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0003DoMainLogicItemOutput {
    private String id;
    private String arsId;
    private BusRouteProcessRegisterRequestDto busRouteProcessRegisterRequestDto;
    private List<BusStationRegisterRequestDto> busStationRegisterRequestDtoList;
}
