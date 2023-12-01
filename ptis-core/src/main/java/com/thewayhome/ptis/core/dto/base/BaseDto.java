package com.thewayhome.ptis.core.dto.base;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
public abstract class BaseDto implements Serializable {
    private String operatorId;
}
