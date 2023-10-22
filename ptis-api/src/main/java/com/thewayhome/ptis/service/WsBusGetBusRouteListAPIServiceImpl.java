package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public class WsBusGetBusRouteListAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetBusRouteListAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getbusroutelist}") String path
    ) {
        super(apiKey, endpoint, path, GetBusRouteListAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getBusRouteList(@Valid GetBusRouteListAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("strSrch", req.getBusRouteNo());

        return this.getDataFromOpenAPI(queryParams);
    }
}
