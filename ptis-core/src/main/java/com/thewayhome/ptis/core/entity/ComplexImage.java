package com.thewayhome.ptis.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "complex_image")
public class ComplexImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "real_complex_id")
    private RealComplex realComplex;

    @Column(name = "url")
    private String url;

    @Column(name = "is_main")
    private Boolean isMain;

}
