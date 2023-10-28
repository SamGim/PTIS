package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.BaseRequestVo;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusStationRegisterReqVo extends BaseRequestVo {
    private String busStationId;
    private String busStationName;
    private String busStationNo;
    private String busStationPosX;
    private String busStationPosY;
}
