package com.thewayhome.ptis.core.dto.request;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class CompanyRequestDto {
    private Long id;
    private String companyName;
    private String address;
    private Double latitude;
    private Double longitude;
}
