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
        name = "BusRouteProcess",
        indexes = {
                @Index(name = "BusRouteProcess_U1", columnList = "id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteProcess extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private BusRoute busRoute;

    /*
     * bus_route_gat_stcd
     * 버스노선 정보의 수집 상태를 구분하는 상태코드
     *
     * 00: 미수집
     * 01: 버스노선 기본정보 수집 (API)
     * 02: 버스노선 상세정보 수집 (API)
     * 99: 수집오류
     */
    @Column(name="bus_route_gat_stcd", nullable = false, columnDefinition = "VARCHAR(2) DEFAULT '00'")
    private String busRouteGatheringStatusCode;

    /*
     * bus_route_fst_gat_dt
     * 버스노선 정보의 최초 수집 일자
     */
    @Column(name="bus_route_fst_gat_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String busRouteFirstGatheringDate;

    /*
     * bus_route_lst_gat_dt
     * 버스노선 정보의 마지막 수집 일자
     */
    @Column(name="bus_route_lst_gat_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String busRouteLastGatheringDate;

    /*
     * bus_station_gat_stcd
     * 버스노선 ID를 통한 버스정류장 정보의 수집 상태를 구분하는 상태코드
     *
     * 00: 미수집
     * 01: 버스정류장 정보 수집 (API)
     * 99: 수집오류
     */
    @Column(name="bus_station_gat_stcd", nullable = false, columnDefinition = "VARCHAR(2) DEFAULT '00'")
    private String busStationGatheringStatusCode;

    /*
     * bus_station_fst_gat_dt
     * 버스정류장 정보의 최초 수집 일자
     */
    @Column(name="bus_station_fst_gat_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String busStationFirstGatheringDate;

    /*
     * bus_station_lst_gat_dt
     * 버스정류장 정보의 마지막 수집 일자
     */
    @Column(name="bus_station_lst_gat_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String busStationLastGatheringDate;
}
