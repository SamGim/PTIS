package com.thewayhome.ptis.batch.job.b0002;

import com.thewayhome.ptis.core.dto.request.BusRouteRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationProcessRegisterRequestDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0002DoMainLogicItemOutput {
    private String id;
    private String arsId;
    private BusStationProcessRegisterRequestDto busStationProcessRegisterRequestDto;
    private List<BusRouteRegisterRequestDto> busRouteRegisterRequestDtoList;
}
