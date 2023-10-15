package com.thewayhome.ptis.vo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlRootElement(name = "busStationAroundList")
public class BusStationAroundList {
    @XmlElement(name = "centerYn")
    private String centerYn;

    @XmlElement(name = "districtCd")
    private int districtCd;

    @XmlElement(name = "mobileNo")
    private Long mobileNo;

    @XmlElement(name = "regionName")
    private String regionName;

    @XmlElement(name = "stationId")
    private Long stationId;

    @XmlElement(name = "stationName")
    private String stationName;

    @XmlElement(name = "x")
    private Double x;

    @XmlElement(name = "y")
    private Double y;

    @XmlElement(name = "distance")
    private int distance;
}
