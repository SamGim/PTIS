package com.thewayhome.ptis.core.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteProcessRegisterReqDto extends BaseRequestDto {
    private String id;
    private String gatheringStatusCode;
    private String selfGatheringStatusCode;
    private String stationGatheringStatusCode;
}
