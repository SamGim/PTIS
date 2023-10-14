package com.thewayhome.ptis.vo.wsbus.getroutebystation;

import com.thewayhome.ptis.vo.wsbus.IMsgHeader;
import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@XmlRootElement(name = "ServiceResult")
public class ServiceResultErrorVoImpl implements IServiceResult {

    @XmlElement(name = "msgHeader")
    private MsgHeaderImpl msgHeader;

    @Getter
    public static class MsgHeaderImpl implements IMsgHeader {
        @XmlElement(name = "headerCd")
        private String headerCd;

        @XmlElement(name = "headerMsg")
        private String headerMsg;

        @XmlElement(name = "itemCount")
        private int itemCount;
    }

}
