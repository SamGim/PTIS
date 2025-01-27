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
@Table(
        name = "GymProcess",
        indexes = {
                @Index(name = "GymProcess_U1", columnList = "id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class GymProcess extends BaseEntity {

    @Id
    @Column(name="id", length = 12, nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "id")
    private Gym gym;

    /*
     * gym_gat_stcd
     * 헬스장 정보의 수집 상태를 구분하는 상태코드
     *
     * 00: 미수집
     * 01: 헬스장 기본정보 수집 (API)
     * 02: 헬스장 상세정보 수집 (API)
     * 99: 수집오류
     */
    @Column(name="gym_gat_stcd", nullable = false, columnDefinition = "VARCHAR(2) DEFAULT '00'")
    private String gymGatheringStatusCode;

    /*
     * gym_lst_gat_dt
     * 헬스장 정보의 최초 수집 일자
     */
    @Column(name="gym_fst_gat_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String gymFirstGatheringDate;

    /*
     * gym_lst_gat_dt
     * 헬스장 정보의 마지막 수집 일자
     */
    @Column(name="gym_lst_gat_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String gymLastGatheringDate;

    /*
     * node_cre_stcd
     * 헬스장 정보를 통한 노드의 생성 상태를 구분하는 상태코드
     *
     * 00: 미생성
     * 01: 노드 기본 정보 생성
     * 99: 생성오류
     */
    @Column(name="node_cre_stcd", nullable = false, columnDefinition = "VARCHAR(2) DEFAULT '00'")
    private String nodeCreationStatusCode;

    /*
     * node_fst_cre_dt
     * 노드 정보의 최초 생성 일자
     */
    @Column(name="node_fst_cre_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String nodeFirstCreationDate;

    /*
     * node_lst_cre_dt
     * 노드 정보의 마지막 생성 일자
     */
    @Column(name="node_lst_cre_dt", nullable = false, columnDefinition = "VARCHAR(8) DEFAULT ' '")
    private String nodeLastCreationDate;

    /*
     * node
     * 생성한 노드 정보
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private Node node;
}
