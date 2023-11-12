package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name="restaurant_gat_stcd", nullable = false)
    private String restaurantGatheringStatusCode;

    /*
     * fst_gat_dt
     * 버스정류장 정보의 최초 수집 일자
     */
    @Column(name="restaurant_fst_gat_dt", nullable = false)
    private String restaurantFirstGatheringDate;

    /*
     * lst_gat_dt
     * 버스정류장 정보의 마지막 수집 일자
     */
    @Column(name="restaurant_lst_gat_dt", nullable = false)
    private String restaurantLastGatheringDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Restaurant restaurant;
}
