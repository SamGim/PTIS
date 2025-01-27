package com.thewayhome.ptis.api.vo.tago;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusSttnInfoInqireServiceResponseVo {
    @XmlElement(name = "header")
    private BusSttnInfoInqireServiceResponseHeader busSttnInfoInqireServiceResponseHeader;

    @XmlElement(name = "body")
    private BusSttnInfoInqireServiceResponseBody busSttnInfoInqireServiceResponseBody;
}

