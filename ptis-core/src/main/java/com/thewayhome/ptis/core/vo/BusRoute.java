package com.thewayhome.ptis.core.vo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
        name = "BusRoute",
        indexes = {
                @Index(name = "BusRoute_U1", columnList = "id"),
                @Index(name = "BusRoute_X1", columnList = "bus_route_id"),
                @Index(name = "BusRoute_X2", columnList = "bus_route_name")
        }
)
public class BusRoute extends AbstractDataEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="bus_route_id", nullable = false)
    @Size(max=12)
    private String busRouteId;

    @Column(name="bus_route_name", nullable = false)
    @Size(max=100)
    private String busRouteName;

    @Column(name="bus_route_no", nullable = false)
    @Size(max=4)
    private String busRouteNo;

    @Column(name="bus_route_sub_no", nullable = true)
    @Size(max=10)
    private String busRouteSubNo;

    @ManyToOne
    @JoinColumn(name = "bus_station_st", nullable = true)
    private BusStation busStationSt;

    @ManyToOne
    @JoinColumn(name = "bus_station_ed", nullable = true)
    private BusStation busStationEd;

    @ManyToMany
    @JoinTable(
            name = "bus_station_route",
            joinColumns = @JoinColumn(name = "bus_route_id"),
            inverseJoinColumns = @JoinColumn(name = "bus_station_id")
    )
    @OrderColumn(name = "route_order")
    private List<BusStation> stations = new ArrayList<>();

}
