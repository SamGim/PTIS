package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RestaurantProcessVo extends BaseDto {
    private String id;
    private String gatheringStatusCode;
    private String firstGatheringDate;
    private String lastGatheringDate;
    private RestaurantVo restaurant;
}
