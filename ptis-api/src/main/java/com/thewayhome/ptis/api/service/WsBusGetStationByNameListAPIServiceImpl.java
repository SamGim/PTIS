package com.thewayhome.ptis.api.service;

import com.thewayhome.ptis.api.vo.wsbus.CommAPIErrRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.GetStationByNameListAPINrmRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.GetStationByNameListAPIReqVo;
import com.thewayhome.ptis.api.vo.wsbus.IServiceResult;
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
            @Value("${openapi.arrinfo.bus.path.getstationbynamelist}") String path
    ) {
        super(apiKey, endpoint, path, GetStationByNameListAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getStationByNameList(@Valid GetStationByNameListAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("stSrch", req.getStSrch());

        return this.getDataFromOpenAPI(queryParams);
    }
}
