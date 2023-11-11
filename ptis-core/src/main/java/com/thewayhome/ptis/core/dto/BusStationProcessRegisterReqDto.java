package com.thewayhome.ptis.core.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusStationProcessRegisterReqDto extends BaseRequestDto {
    private String id;
    private String gatheringStatusCode;
    private String selfGatheringStatusCode;
    private String routeGatheringStatusCode;
}
