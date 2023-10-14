package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class WsBusGetRouteByStationAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetRouteByStationAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getroutebystation}") String path
    ) {
        super(apiKey, endpoint, path, GetRouteByStationAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getRouteByStation(@Valid GetRouteByStationAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("arsId", req.getArsId());

        return this.getDataFromOpenAPI(queryParams);
    }
}
