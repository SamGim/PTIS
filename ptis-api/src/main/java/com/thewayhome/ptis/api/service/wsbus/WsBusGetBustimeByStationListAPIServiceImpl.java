package com.thewayhome.ptis.api.service.wsbus;

import com.thewayhome.ptis.api.service.AbstractWsBusAPIService;
import com.thewayhome.ptis.api.vo.wsbus.CommAPIErrRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.GetBustimeByStationListAPINrmRespVoImpl;
import com.thewayhome.ptis.api.vo.wsbus.GetBustimeByStationListAPIReqVo;
import com.thewayhome.ptis.api.vo.wsbus.IServiceResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class WsBusGetBustimeByStationListAPIServiceImpl extends AbstractWsBusAPIService {
    public WsBusGetBustimeByStationListAPIServiceImpl(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getbustimebystationlist}") String path
    ) {
        super(apiKey, endpoint, path, GetBustimeByStationListAPINrmRespVoImpl.class, CommAPIErrRespVoImpl.class);
    }

    public Mono<IServiceResult> getBustimeByStationList(@Valid GetBustimeByStationListAPIReqVo req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("arsId", req.getArsId());
        queryParams.add("busRouteId", req.getBusRouteId());

        return this.getDataFromOpenAPI(queryParams);
    }
}
