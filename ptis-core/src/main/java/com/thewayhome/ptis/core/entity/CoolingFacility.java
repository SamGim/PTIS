package com.thewayhome.ptis.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cooling_facility")
public class CoolingFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    // 벽걸이형
    @Column(name = "wall_mounted", columnDefinition = "boolean default false")
    private Boolean wallMounted;

    // 스탠드형
    @Column(name = "stand", columnDefinition = "boolean default false")
    private Boolean stand;

    // 창문형
    @Column(name = "window_mounted", columnDefinition = "boolean default false")
    private Boolean windowMounted;

    // 천장형
    @Column(name = "ceiling", columnDefinition = "boolean default false")
    private Boolean ceiling;

    // 분리형
    @Column(name = "separation", columnDefinition = "boolean default false")
    private Boolean separation;

    // 중앙공조
    @Column(name = "central_air_conditioning", columnDefinition = "boolean default false")
    private Boolean centralAirConditioning;

    // 이동형
    @Column(name = "mobile", columnDefinition = "boolean default false")
    private Boolean mobile;
}
