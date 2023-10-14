package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class WsBusGetStationByNameListAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetStationByNameListAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getstationbyname}") String path
    ) {
        super(apiKey, endpoint, path, GetStationByNameAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getStationByNameList(@Valid GetStationByNameAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("stSrch", req.getStSrch());

        return this.getDataFromOpenAPI(queryParams);
    }
}
