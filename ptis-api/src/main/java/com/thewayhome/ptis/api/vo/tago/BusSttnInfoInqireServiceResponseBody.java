package com.thewayhome.ptis.api.vo.tago;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@XmlRootElement(name = "body")
public class BusSttnInfoInqireServiceResponseBody {
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<BusSttnInfoInqireServiceResponseBodyItem> items;

    @XmlElement(name = "numOfRows")
    private int numOfRows;

    @XmlElement(name = "pageNo")
    private int pageNo;

    @XmlElement(name = "totalCount")
    private int totalCount;
}
