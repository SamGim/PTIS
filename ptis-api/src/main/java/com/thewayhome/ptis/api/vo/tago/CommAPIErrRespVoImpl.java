package com.thewayhome.ptis.api.vo.tago;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "response")
public class CommAPIErrRespVoImpl implements IResponse {

    private Header header;

    @Data
    private static class Header implements IHeader {
        @XmlElement(name = "resultCode")
        private String resultCode;

        @XmlElement(name = "resultMsg")
        private String resultMsg;
    }

}
