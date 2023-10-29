package com.thewayhome.ptis.core.vo;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusStationProcessRegisterReqVo extends BaseRequestVo {
    private String id;
    private String gatheringStatusCode;
}
