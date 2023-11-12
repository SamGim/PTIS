package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RestaurantRegisterProcessRequestDto extends BaseDto {
    private String id;
    private String restaurantFirstGatheringDate;
    private String restaurantLastGatheringDate;
    private String restaurantGatheringStatusCode;
}
