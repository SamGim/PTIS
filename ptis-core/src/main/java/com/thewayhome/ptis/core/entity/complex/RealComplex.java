package com.thewayhome.ptis.core.entity.complex;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "real_complex")
public class RealComplex extends BaseEntity {
    @Id
    @Column(name = "id")
    private Long id;
    // 매물 이름
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "nearest_node_id", nullable = true)
    private String nearestNodeId;

    @Column(name = "nearest_node_time", nullable = true)
    private Long nearestNodeTime;
}
