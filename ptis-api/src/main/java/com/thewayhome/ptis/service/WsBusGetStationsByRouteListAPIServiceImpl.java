package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.CommAPIErrRespVoImpl;
import com.thewayhome.ptis.vo.wsbus.GetStationsByRouteListAPINrmRespVoImpl;
import com.thewayhome.ptis.vo.wsbus.GetStationsByRouteListAPIReqVo;
import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public class WsBusGetStationsByRouteListAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetStationsByRouteListAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getstationsbyroutelist}") String path
    ) {
        super(apiKey, endpoint, path, GetStationsByRouteListAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getDataFromOpenAPI(@Valid GetStationsByRouteListAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("busRouteId", req.getBusRouteId());

        return this.getDataFromOpenAPI(queryParams);
    }
}