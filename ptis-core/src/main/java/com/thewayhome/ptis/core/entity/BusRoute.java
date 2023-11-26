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
        name = "BusRoute",
        indexes = {
                @Index(name = "BusRoute_U1", columnList = "id"),
                @Index(name = "BusRoute_X1", columnList = "bus_route_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class BusRoute extends BaseEntity {
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
}
