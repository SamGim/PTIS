package com.thewayhome.ptis.vo;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@XmlRootElement(name = "getStationByName")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetStationByNameResponseVo {
//    @XmlAttribute // 루트 엘리먼트 속성 값
//    private int empid;

    @XmlElement(name = "busStationAroundList") // 하위 엘리먼트
    private List<BusStationAroundList> busStationAroundList;

//    @XmlElement(name = "name")
//    private String name;
//
//    @XmlElement(name = "salary")
//    private float salary;
}
