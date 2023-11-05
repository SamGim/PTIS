package com.thewayhome.ptis.batch.job.b0004;

import com.thewayhome.ptis.core.vo.BusRouteProcessRegisterReqVo;
import com.thewayhome.ptis.core.vo.BusStationRegisterReqVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0004DoMainLogicItemOutput {
    private String nodeId;
    private String nodeName;
    private String nodePosX;
    private String nodePosY;
    private String nodeAddress;
    private String nodeType;
}
