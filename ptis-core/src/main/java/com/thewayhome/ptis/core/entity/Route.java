package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.AbstractDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Route extends AbstractDataEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @ManyToOne
    @JoinColumn(name = "station_st", nullable = false)
    private Station stationSt;

    @ManyToOne
    @JoinColumn(name = "station_ed", nullable = false)
    private Station stationEd;

    @Column(name="cost_base", nullable = false)
    private long costBase;

    @Column(name="cost_sub", nullable = false)
    private long costSub;

    @Column(name="cost_add", nullable = false)
    private long costAdd;
}
