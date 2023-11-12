package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRegisterReqDto extends BaseDto {
    private String restaurantName;
    private String restaurantId;
    private String restaurantPosX;
    private String restaurantPosY;
    private String restaurantAddress;
    private String restaurantType;
}
