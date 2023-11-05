package com.thewayhome.ptis.core.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRegisterReqDto extends BaseRequestDto {
    private String restaurantName;
    private String restaurantId;
    private String restaurantPosX;
    private String restaurantPosY;
    private String restaurantAddress;
    private String restaurantType;
}
