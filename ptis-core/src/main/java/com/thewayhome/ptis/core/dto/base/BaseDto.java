package com.thewayhome.ptis.core.dto.base;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
public abstract class BaseDto implements Serializable {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @NonNull
    private String operatorId;
}
