package com.thewayhome.ptis.batch.job.b0014;

import com.thewayhome.ptis.core.vo.BusStationVo;
import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B0014DoMainLogicItemInput {
    private BusStationVo targetNode;
}
