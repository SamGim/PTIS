package com.thewayhome.ptis.vo.wsbus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "ServiceResult")
public class GetStationsByRouteListAPINrmRespVoImpl implements IServiceResult {

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

            @XmlElement(name = "arsId")
            private String arsId;

            @XmlElement(name = "beginTm")
            private String beginTm;

            @XmlElement(name = "busRouteAbrv")
            private String busRouteAbrv;

            @XmlElement(name = "busRouteId")
            private String busRouteId;

            @XmlElement(name = "busRouteNm")
            private String busRouteNm;

            @XmlElement(name = "direction")
            private String direction;

            @XmlElement(name = "gpsX")
            private String gpsX;

            @XmlElement(name = "gpsY")
            private String gpsY;

            @XmlElement(name = "lastTm")
            private String lastTm;

            @XmlElement(name = "posX")
            private String posX;

            @XmlElement(name = "posY")
            private String posY;

            @XmlElement(name = "routeType")
            private String routeType;

            @XmlElement(name = "sectSpd")
            private String sectSpd;

            @XmlElement(name = "section")
            private String section;

            @XmlElement(name = "seq")
            private String seq;

            @XmlElement(name = "station")
            private String station;

            @XmlElement(name = "stationNm")
            private String stationNm;

            @XmlElement(name = "stationNo")
            private String stationNo;

            @XmlElement(name = "transYn")
            private String transYn;

            @XmlElement(name = "fullSectDist")
            private String fullSectDist;

            @XmlElement(name = "trnstnid")
            private String trnstnid;
        }
    }
}