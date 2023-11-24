package com.thewayhome.ptis.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "heating_facility")
public class HeatingFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    // 중앙난방
    @Column(name = "central_heating", columnDefinition = "boolean default false")
    private Boolean centralHeating;

    // 개별난방
    @Column(name = "individual_heating", columnDefinition = "boolean default false")
    private Boolean individualHeating;

    // 지역난방
    @Column(name = "regional_heating", columnDefinition = "boolean default false")
    private Boolean regionalHeating;
}
