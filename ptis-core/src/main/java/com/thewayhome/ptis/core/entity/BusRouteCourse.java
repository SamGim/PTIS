package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@Table(
        name = "BusRouteCourse",
        indexes = {
                @Index(name = "BusRouteCourse_U1", columnList = "id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteCourse extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bus_route_id", nullable = false)
    @Size(max=12)
    private BusRoute busRoute;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bus_station_id", nullable = false)
    @Size(max=12)
    private BusStation busStation;

    @Column(name = "first_bus_time", nullable = true)
    private LocalTime firstBusTime;

    @Column(name = "last_bus_time", nullable = true)
    private LocalTime lastBusTime;
}
