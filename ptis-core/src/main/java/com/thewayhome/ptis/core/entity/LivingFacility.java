package com.thewayhome.ptis.core.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "living_facility")
public class LivingFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    // 에어컨
    @Column(name = "air_conditioner", columnDefinition = "boolean default false")
    private Boolean airConditioner;

    // 냉장고
    @Column(name = "refrigerator", columnDefinition = "boolean default false")
    private Boolean refrigerator;

    // 세탁기
    @Column(name = "washing_machine", columnDefinition = "boolean default false")
    private Boolean washingMachine;

    // 가스레인지
    @Column(name = "gas_range", columnDefinition = "boolean default false")
    private Boolean gasRange;

    // 전자레인지
    @Column(name = "microwave", columnDefinition = "boolean default false")
    private Boolean microwave;

    // 인덕션
    @Column(name = "induction", columnDefinition = "boolean default false")
    private Boolean induction;

    // 책상
    @Column(name = "desk", columnDefinition = "boolean default false")
    private Boolean desk;

    // 책장
    @Column(name = "bookshelf", columnDefinition = "boolean default false")
    private Boolean bookshelf;

    // 식탁
    @Column(name = "dining_table", columnDefinition = "boolean default false")
    private Boolean diningTable;

    // 의자
    @Column(name = "chair", columnDefinition = "boolean default false")
    private Boolean chair;

    // 침대
    @Column(name = "bed", columnDefinition = "boolean default false")
    private Boolean bed;

    // 옷장
    @Column(name = "closet", columnDefinition = "boolean default false")
    private Boolean closet;

    // 건조기
    @Column(name = "dryer", columnDefinition = "boolean default false")
    private Boolean dryer;

    // 붙박이장
    @Column(name = "built_in_closet", columnDefinition = "boolean default false")
    private Boolean builtInCloset;

    // 소파
    @Column(name = "sofa", columnDefinition = "boolean default false")
    private Boolean sofa;

    // 신발장
    @Column(name = "shoe_closet", columnDefinition = "boolean default false")
    private Boolean shoeCloset;

    // 샤워부스
    @Column(name = "shower_booth", columnDefinition = "boolean default false")
    private Boolean showerBooth;

    // 욕조
    @Column(name = "bathtub", columnDefinition = "boolean default false")
    private Boolean bathtub;

    // 비데
    @Column(name = "bidet", columnDefinition = "boolean default false")
    private Boolean bidet;

    // 싱크대
    @Column(name = "sink", columnDefinition = "boolean default false")
    private Boolean sink;

    // 식기세척기
    @Column(name = "dishwasher", columnDefinition = "boolean default false")
    private Boolean dishwasher;

    // 인덕션
    @Column(name = "induction_stove", columnDefinition = "boolean default false")
    private Boolean inductionStove;

    // 가스오븐
    @Column(name = "gas_oven", columnDefinition = "boolean default false")
    private Boolean gasOven;
}
