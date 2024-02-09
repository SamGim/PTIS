package com.thewayhome.ptis.core.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class RealComplexRequestDto {

    private Long id;


    private String name;


    private Double latitude;


    private Double longitude;
}
