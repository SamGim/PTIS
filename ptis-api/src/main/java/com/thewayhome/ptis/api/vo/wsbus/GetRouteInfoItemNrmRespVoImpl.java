package com.thewayhome.ptis.api.vo.wsbus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "ServiceResult")
public class GetRouteInfoItemNrmRespVoImpl implements IServiceResult {

    @XmlElement(name = "msgHeader")
    private MsgHeaderImpl msgHeader;

    @XmlElement(name = "msgBody")
    private MsgBody msgBody;

    @Getter
    public static class MsgHeaderImpl implements IMsgHeader {
        @XmlElement(name = "headerCd")
        private String headerCd;

        @XmlElement(name = "headerMsg")
        private String headerMsg;

        @XmlElement(name = "itemCount")
        private int itemCount;
    }

    @Getter
    public static class MsgBody {
        @XmlElement(name = "itemList")
        private List<ItemList> itemList;

        @Getter
        public static class ItemList {

            @XmlElement(name = "busRouteAbrv")
            private String busRouteAbrv;

            @XmlElement(name = "busRouteId")
            private String busRouteId;

            @XmlElement(name = "busRouteNm")
            private String busRouteNm;

            @XmlElement(name = "corpNm")
            private String corpNm;

            @XmlElement(name = "edStationNm")
            private String edStationNm;

            @XmlElement(name = "firstBusTm")
            private String firstBusTm;

            @XmlElement(name = "firstLowTm")
            private String firstLowTm;

            @XmlElement(name = "lastBusTm")
            private String lastBusTm;

            @XmlElement(name = "lastBusYn")
            private String lastBusYn;

            @XmlElement(name = "lastLowTm")
            private String lastLowTm;

            @XmlElement(name = "length")
            private String length;

            @XmlElement(name = "routeType")
            private String routeType;

            @XmlElement(name = "stStationNm")
            private String stStationNm;

            @XmlElement(name = "term")
            private String term;
        }
    }
}