package com.thewayhome.ptis.vo.wsbus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "ServiceResult")
public class GetStationByUidAPINormalResponseVoImpl implements IServiceResult {

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
            @XmlElement(name = "adirection")
            private String adirection;

            @XmlElement(name = "arrmsg1")
            private String arrmsg1;

            @XmlElement(name = "arrmsg2")
            private String arrmsg2;

            @XmlElement(name = "arrmsgSec1")
            private String arrmsgSec1;

            @XmlElement(name = "arrmsgSec2")
            private String arrmsgSec2;

            @XmlElement(name = "arsId")
            private String arsId;

            @XmlElement(name = "busRouteAbrv")
            private String busRouteAbrv;

            @XmlElement(name = "busRouteId")
            private String busRouteId;

            @XmlElement(name = "busType1")
            private String busType1;

            @XmlElement(name = "busType2")
            private String busType2;

            @XmlElement(name = "congestion1")
            private String congestion1;

            @XmlElement(name = "congestion2")
            private String congestion2;

            @XmlElement(name = "deTourAt")
            private String deTourAt;

            @XmlElement(name = "firstTm")
            private String firstTm;

            @XmlElement(name = "gpsX")
            private String gpsX;

            @XmlElement(name = "gpsY")
            private String gpsY;

            @XmlElement(name = "isArrive1")
            private String isArrive1;

            @XmlElement(name = "isArrive2")
            private String isArrive2;

            @XmlElement(name = "isFullFlag1")
            private String isFullFlag1;

            @XmlElement(name = "isFullFlag2")
            private String isFullFlag2;

            @XmlElement(name = "isLast1")
            private String isLast1;

            @XmlElement(name = "isLast2")
            private String isLast2;

            @XmlElement(name = "lastTm")
            private String lastTm;

            @XmlElement(name = "nextBus")
            private String nextBus;

            @XmlElement(name = "nxtStn")
            private String nxtStn;

            @XmlElement(name = "posX")
            private String posX;

            @XmlElement(name = "posY")
            private String posY;

            @XmlElement(name = "repTm1")
            private String repTm1;

            @XmlElement(name = "rerdieDiv1")
            private String rerdieDiv1;

            @XmlElement(name = "rerdieDiv2")
            private String rerdieDiv2;

            @XmlElement(name = "rerideNum1")
            private String rerideNum1;

            @XmlElement(name = "rerideNum2")
            private String rerideNum2;

            @XmlElement(name = "routeType")
            private String routeType;

            @XmlElement(name = "rtNm")
            private String rtNm;

            @XmlElement(name = "sectNm")
            private String sectNm;

            @XmlElement(name = "sectOrd1")
            private String sectOrd1;

            @XmlElement(name = "sectOrd2")
            private String sectOrd2;

            @XmlElement(name = "stId")
            private String stId;

            @XmlElement(name = "stNm")
            private String stNm;

            @XmlElement(name = "staOrd")
            private String staOrd;

            @XmlElement(name = "stationNm1")
            private String stationNm1;

            @XmlElement(name = "stationNm2")
            private String stationNm2;

            @XmlElement(name = "stationTp")
            private String stationTp;

            @XmlElement(name = "term")
            private String term;

            @XmlElement(name = "traSpd1")
            private String traSpd1;

            @XmlElement(name = "traSpd2")
            private String traSpd2;

            @XmlElement(name = "traTime1")
            private String traTime1;

            @XmlElement(name = "traTime2")
            private String traTime2;

            @XmlElement(name = "vehId1")
            private String vehId1;

            @XmlElement(name = "vehId2")
            private String vehId2;
        }
    }
}