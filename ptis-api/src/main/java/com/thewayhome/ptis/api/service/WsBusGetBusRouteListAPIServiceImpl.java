package com.thewayhome.ptis.api.service;

import com.thewayhome.ptis.api.vo.wsbus.GetBusRouteListAPINrmRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.GetBusRouteListAPIReqVo;
import com.thewayhome.ptis.api.vo.wsbus.CommAPIErrRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.IServiceResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
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
