package com.thewayhome.ptis.api.vo.wsbus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "ServiceResult")
public class GetRouteByStationListAPINrmRespVoImpl implements IServiceResult {

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

            @XmlElement(name = "busRouteType")
            private int busRouteType;

            @XmlElement(name = "firstBusTm")
            private String firstBusTm;

            @XmlElement(name = "firstBusTmLow")
            private String firstBusTmLow;

            @XmlElement(name = "lastBusTm")
            private String lastBusTm;

            @XmlElement(name = "lastBusTmLow")
            private String lastBusTmLow;

            @XmlElement(name = "length")
            private int length;

            @XmlElement(name = "nextBus")
            private int nextBus;

            @XmlElement(name = "stBegin")
            private String stBegin;

            @XmlElement(name = "stEnd")
            private String stEnd;

            @XmlElement(name = "term")
            private int term;
        }
    }
}