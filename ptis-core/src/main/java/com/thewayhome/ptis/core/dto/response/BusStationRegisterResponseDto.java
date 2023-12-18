package com.thewayhome.ptis.core.dto.response;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import com.thewayhome.ptis.core.vo.BusStationVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BusStationRegisterResponseDto extends BaseDto {
    private BusStationVo busStationVo;
    private Boolean isRegistered;
}
