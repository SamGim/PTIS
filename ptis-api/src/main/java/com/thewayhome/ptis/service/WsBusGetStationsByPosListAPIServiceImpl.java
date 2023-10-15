package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class WsBusGetStationsByPosListAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetStationsByPosListAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getstationsbyposlist}") String path
    ) {
        super(apiKey, endpoint, path, GetStationsByPosListAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getStationsByPosList(@Valid GetStationsByPosListAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("tmX", req.getTmX());
        queryParams.add("tmY", req.getTmY());
        queryParams.add("radius", req.getRadius());

        return this.getDataFromOpenAPI(queryParams);
    }
}
