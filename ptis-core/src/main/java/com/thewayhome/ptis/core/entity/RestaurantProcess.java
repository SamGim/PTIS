package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class RestaurantProcess extends BaseEntity {

    @Id
    @Column(name="id", length = 12, nullable = false)
    private String id;


    /*
     * gat_stcd
     * 버스정류장 정보의 수집 상태 코드
     * 0: 최초 수집
     * 1: x좌표, y좌표가 null일 경우
     */

    @Column(name="gat_stcd", nullable = false)
    private String gatheringStatusCode;

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
    private Restaurant restaurant;
}
