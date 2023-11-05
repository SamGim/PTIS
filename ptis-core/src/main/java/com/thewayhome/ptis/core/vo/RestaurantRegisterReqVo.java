package com.thewayhome.ptis.core.vo;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRegisterReqVo extends BaseRequestVo{
    private String restaurantName;
    private String restaurantId;
    private String restaurantPosX;
    private String restaurantPosY;
    private String restaurantAddress;
    private String restaurantType;
}
