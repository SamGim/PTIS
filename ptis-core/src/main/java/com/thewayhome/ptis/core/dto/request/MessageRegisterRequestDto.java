package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class MessageRegisterRequestDto extends BaseDto {
    private String id;
    private String msgHeader;
    private String msgBody;
    private String rawMessage;
}
