package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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

    @JoinColumn(columnDefinition = "bus_route_id", nullable = false)
    @Size(max=12)
    private BusRoute busRoute;

    @JoinColumn(columnDefinition = "bus_station_id", nullable = false)
    @Size(max=12)
    private BusStation busStation;

    @Column(columnDefinition = "first_bus_time", nullable = false)
    private LocalDateTime firstBusTime;

    @Column(columnDefinition = "last_bus_time", nullable = false)
    private LocalDateTime lastBusTime;
}
