package com.thewayhome.ptis.core.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "etc_facility")
public class EtcFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    // 화재경보기
    @Column(name = "fire_alarm", columnDefinition = "boolean default false")
    private Boolean fireAlarm;

    // 무인택배함
    @Column(name = "unmanned_parcel_box", columnDefinition = "boolean default false")
    private Boolean unmannedParcelBox;

    // 베란다
    @Column(name = "veranda", columnDefinition = "boolean default false")
    private Boolean veranda;

    // 테라스
    @Column(name = "terrace", columnDefinition = "boolean default false")
    private Boolean terrace;

    // 소화기
    @Column(name = "fire_extinguisher", columnDefinition = "boolean default false")
    private Boolean fireExtinguisher;
}
