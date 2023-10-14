package com.thewayhome.ptis.vo.wsbus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "ServiceResult")
public class GetStationByNameAPINrmRespVoImpl implements IServiceResult {

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

            @XmlElement(name = "posX")
            private String posX;

            @XmlElement(name = "posY")
            private String posY;

            @XmlElement(name = "stId")
            private String stId;

            @XmlElement(name = "stNm")
            private String stNm;

            @XmlElement(name = "tmX")
            private String tmX;

            @XmlElement(name = "tmY")
            private String tmY;
        }
    }
}