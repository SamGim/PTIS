package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RestaurantProcessVo extends BaseVo {
    private String id;
    private String restaurantFirstGatheringDate;
    private String restaurantLastGatheringDate;
    private String restaurantGatheringStatusCode;
    private RestaurantVo restaurant;
}
