package com.thewayhome.ptis.api.service;

import com.thewayhome.ptis.api.vo.tago.CommAPIErrRespVoImpl;
import com.thewayhome.ptis.api.vo.tago.GetRouteInfoItemAPINrmRespVoImpl;
import com.thewayhome.ptis.api.vo.tago.GetRouteInfoItemAPIReqVo;
import com.thewayhome.ptis.api.vo.tago.IResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class TagoBusGetRouteInfoIemAPIServiceImpl extends AbstractTagoBusAPIService {
    public TagoBusGetRouteInfoIemAPIServiceImpl(
            @Value("${openapi.tago.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.tago.path.getrouteinfoiem}") String path
    ) {
        super(apiKey, endpoint, path, GetRouteInfoItemAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IResponse> getRouteInfoItem(@Valid GetRouteInfoItemAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("_type", req.get_type());
        queryParams.add("cityCode", req.getCityCode());
        queryParams.add("routeId", req.getRouteId());

        return this.getDataFromOpenAPI(queryParams);
    }
}
