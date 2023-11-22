package com.thewayhome.ptis.core.dto.request;

import com.thewayhome.ptis.core.dto.base.BaseDto;
import com.thewayhome.ptis.core.entity.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GymRegisterProcessRequestDto extends BaseDto {
    private String id;
    private String gymFirstGatheringDate;
    private String gymLastGatheringDate;
    private String gymGatheringStatusCode;
    private String nodeFirstCreationDate;
    private String nodeLastCreationDate;
    private String nodeCreationStatusCode;
    private Node node;
}
