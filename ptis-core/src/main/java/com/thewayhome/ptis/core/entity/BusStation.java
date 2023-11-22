package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@Table(
        name = "BusStation",
        indexes = {
                @Index(name = "BusStation_U1", columnList = "id"),
                @Index(name = "BusStation_X1", columnList = "bus_station_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class BusStation extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="bus_station_id", nullable = false)
    @Size(max=12)
    private String busStationId;

    @Column(name="bus_station_name", nullable = false)
    @Size(max=100)
    private String busStationName;

    @Column(name="bus_station_no", nullable = false)
    @Size(min=5,max=5)
    private String busStationNo;

    @Column(name="bus_station_pos_x", nullable = false)
    private Double busStationPosX;

    @Column(name="bus_station_pos_y", nullable = false)
    private Double busStationPosY;
}
