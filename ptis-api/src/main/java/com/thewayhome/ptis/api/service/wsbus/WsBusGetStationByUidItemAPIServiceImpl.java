package com.thewayhome.ptis.api.service.wsbus;

import com.thewayhome.ptis.api.service.AbstractWsBusAPIService;
import com.thewayhome.ptis.api.vo.wsbus.CommAPIErrRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.GetStationByUidItemAPINrmRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.GetStationByUidItemAPIReqVo;
import com.thewayhome.ptis.api.vo.wsbus.IServiceResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class WsBusGetStationByUidItemAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetStationByUidItemAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getstationbyuiditem}") String path
    ) {
        super(apiKey, endpoint, path, GetStationByUidItemAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getStationByUidItem(@Valid GetStationByUidItemAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("arsId", req.getArsId());

        return this.getDataFromOpenAPI(queryParams);
    }
}
