package com.thewayhome.ptis.core.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class CompanyRequestDto {
    private Long id;
    private String companyName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String nearestNodeId;
    private Long nearestNodeTime;
}
