package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.AbstractDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
        name = "BusStation",
        indexes = {
                @Index(name = "BusStation_U1", columnList = "id"),
                @Index(name = "BusStation_X1", columnList = "bus_station_id"),
                @Index(name = "BusStation_X2", columnList = "bus_station_name")
        }
)
public class BusStation extends AbstractDataEntity {
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
    private double busStationPosX;

    @Column(name="bus_station_pos_y", nullable = false)
    private double busStationPosY;
}
