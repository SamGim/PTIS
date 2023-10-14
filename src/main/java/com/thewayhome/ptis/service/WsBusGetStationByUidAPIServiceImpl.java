package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import com.thewayhome.ptis.vo.wsbus.getstationbyuid.ServiceResultErrorVoImpl;
import com.thewayhome.ptis.vo.wsbus.getstationbyuid.ServiceResultNormalVoImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class WsBusGetStationByUidAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetStationByUidAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getstationbyuid}") String path
    ) {
        super(apiKey, endpoint, path, ServiceResultNormalVoImpl.class, ServiceResultErrorVoImpl.class);
    }

    public Mono<IServiceResult> getStationByUid(String arsId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("arsId", arsId);

        return this.getDataFromOpenAPI(queryParams);
    }
}
