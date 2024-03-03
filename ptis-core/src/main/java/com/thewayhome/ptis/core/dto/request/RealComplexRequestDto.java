package com.thewayhome.ptis.core.dto.request;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class RealComplexRequestDto {

    private Long id;


    private String name;


    private Double latitude;


    private Double longitude;
}
