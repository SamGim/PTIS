package com.thewayhome.ptis.core.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRequestDto implements Serializable {
    private String operatorId;
}
