package com.thewayhome.ptis.vo.wsbus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "ServiceResult")
public class GetStationsByPosListAPINrmRespVoImpl implements IServiceResult {

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

            @XmlElement(name = "dist")
            private String dist;

            @XmlElement(name = "gpsX")
            private String gpsX;

            @XmlElement(name = "gpsY")
            private String gpsY;

            @XmlElement(name = "posX")
            private String posX;

            @XmlElement(name = "posY")
            private String posY;

            @XmlElement(name = "stationId")
            private String stationId;

            @XmlElement(name = "stationNm")
            private String stationNm;

            @XmlElement(name = "stationTp")
            private String stationTp;
        }
    }
}