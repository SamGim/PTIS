package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GymRegisterRequestDto extends BaseDto {
    private String id;
    private String gymName;
    private String gymId;
    private String gymPosX;
    private String gymPosY;
    private String gymAddress;
}
