package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.AbstractDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Station extends AbstractDataEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="station_id", length = 12, nullable = false)
    private String stationId;

    @Column(name="station_pos_x", nullable = false)
    private double stationPosX;

    @Column(name="station_pos_y", nullable = false)
    private double stationPosY;
}
