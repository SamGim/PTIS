package com.thewayhome.ptis.vo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlRootElement(name = "item")
public class BusSttnInfoInqireServiceResponseBodyItem {
    @XmlElement(name = "citycode")
    private Long cityCode;

    @XmlElement(name = "gpslati")
    private Double gpsLati;

    @XmlElement(name = "gpslong")
    private Double gpsLong;

    @XmlElement(name = "nodeid")
    private String nodeId;

    @XmlElement(name = "nodenm")
    private String nodeNm;

    @XmlElement(name = "nodeno")
    private Long nodeNo;
}
