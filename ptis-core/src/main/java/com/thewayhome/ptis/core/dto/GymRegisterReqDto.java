package com.thewayhome.ptis.core.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GymRegisterReqDto extends BaseRequestDto {
    private String gymName;
    private String gymId;
    private String gymPosX;
    private String gymPosY;
    private String gymAddress;
}
