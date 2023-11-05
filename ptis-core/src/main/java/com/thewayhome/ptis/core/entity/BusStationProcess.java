package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
        name = "BusStationProcess",
        indexes = {
                @Index(name = "BusStationProcess_U1", columnList = "id"),
                @Index(name = "BusStationProcess_X1", columnList = "gat_stcd"),
                @Index(name = "BusStationProcess_X2", columnList = "fst_gat_dt"),
                @Index(name = "BusStationProcess_X3", columnList = "lst_gat_dt")
        }
)
public class BusStationProcess extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    /*
     * gat_stcd
     * 버스정류장 정보의 수집 상태를 구분하는 상태코드
     *
     * 00: 미수집
     * 01: 버스정류장 기본정보 수집 (CSV)
     * 02: 버스정류장 상세정보 수집 (API)
     * 99: 수집오류
     */
    @Column(name="gat_stcd", nullable = false)
    private String gatheringStatusCode;

    /*
     * slf_gat_stcd
     * 버스정류장 정보의 수집 상태를 구분하는 상태코드
     *
     * 00: 미수집
     * 01: 버스정류장 기본정보 수집 (API)
     * 02: 버스정류장 상세정보 수집 (API)
     * 99: 수집오류
     */
    @Column(name="slf_gat_stcd", nullable = false)
    private String selfGatheringStatusCode;

    /*
     * route_gat_stcd
     * 버스정류장 ID를 통한 버스노선 정보의 수집 상태를 구분하는 상태코드
     *
     * 00: 미수집
     * 01: 버스노선 정보 수집 (API)
     * 99: 수집오류
     */
    @Column(name="route_gat_stcd", nullable = false)
    private String routeGatheringStatusCode;

    /*
     * fst_gat_dt
     * 버스정류장 정보의 최초 수집 일자
     */
    @Column(name="fst_gat_dt", nullable = false)
    private String firstGatheringDate;

    /*
     * lst_gat_dt
     * 버스정류장 정보의 마지막 수집 일자
     */
    @Column(name="lst_gat_dt", nullable = false)
    private String lastGatheringDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private BusStation busStation;
}
