package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public class WsBusGetRouteInfoItemAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetRouteInfoItemAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getrouteinfoitem}") String path
    ) {
        super(apiKey, endpoint, path, GetRouteInfoItemNrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getRouteInfoItem(@Valid GetRouteInfoItemAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("busRouteId", req.getBusRouteId());

        return this.getDataFromOpenAPI(queryParams);
    }
}
