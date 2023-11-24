package com.thewayhome.ptis.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "maintenance_cost_includings")
public class MaintenanceCostIncludings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "electricity", nullable = false)
    private Boolean electricity;

    @Column(name = "gas", nullable = false)
    private Boolean gas;

    @Column(name = "internet", nullable = false)
    private Boolean internet;

    @Column(name = "water", nullable = false)
    private Boolean water;

    @Column(name = "tv", nullable = false)
    private Boolean tv;

}
