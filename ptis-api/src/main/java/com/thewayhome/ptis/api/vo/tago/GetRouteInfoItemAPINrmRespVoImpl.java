package com.thewayhome.ptis.api.vo.tago;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@XmlRootElement(name = "response")
public class GetRouteInfoItemAPINrmRespVoImpl implements IResponse {

    private Header header;
    private Body body;

    @Getter
    private static class Header implements IHeader {
        @XmlElement(name = "resultCode")
        private String resultCode;

        @XmlElement(name = "resultMsg")
        private String resultMsg;
    }

    @Getter
    private static class Body {
        @XmlElement(name = "items")
        private Items items;

        @Getter
        private static class Items {
            @XmlElement(name = "item")
            private List<Item> item;

            @Getter
            private static class Item {
                @XmlElement(name = "endnodenm")
                private String endNodeNm;

                @XmlElement(name = "endvehicletime")
                private String endVehicleTime;

                @XmlElement(name = "intervalsattime")
                private String intervalSatTime;

                @XmlElement(name = "intervalsuntime")
                private String intervalSunTime;

                @XmlElement(name = "intervaltime")
                private String intervalTime;

                @XmlElement(name = "routeid")
                private String routeId;

                @XmlElement(name = "routeno")
                private String routeNo;

                @XmlElement(name = "routetp")
                private String routeTp;

                @XmlElement(name = "startnodenm")
                private String startNodeNm;

                @XmlElement(name = "startvehicletime")
                private String startVehicleTime;
            }
        }
    }
}
