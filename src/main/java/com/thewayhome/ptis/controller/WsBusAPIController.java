package com.thewayhome.ptis.controller;

import com.thewayhome.ptis.service.WsBusGetRouteByStationAPIServiceImpl;
import com.thewayhome.ptis.service.WsBusGetStationByNameListAPIServiceImpl;
import com.thewayhome.ptis.service.WsBusGetStationByUidAPIServiceImpl;
import com.thewayhome.ptis.vo.wsbus.GetRouteByStationAPIReqVo;
import com.thewayhome.ptis.vo.wsbus.GetStationByNameAPIReqVo;
import com.thewayhome.ptis.vo.wsbus.GetStationByUidAPIReqVo;
import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WsBusAPIController {

    @Autowired
    private WsBusGetStationByNameListAPIServiceImpl wsBusGetStationByNameListAPIService;
    @Autowired
    private WsBusGetStationByUidAPIServiceImpl wsBusGetStationByUidAPIService;
    @Autowired
    private WsBusGetRouteByStationAPIServiceImpl wsBusGetRouteByStationAPIService;

    @GetMapping(name="getStationByNameList", path="stationByName")
    public Mono<IServiceResult> getStationByNameList(@RequestParam String stationName) {
        return wsBusGetStationByNameListAPIService.getStationByNameList(
                GetStationByNameAPIReqVo.builder()
                        .stSrch(stationName)
                        .build()
        );
    }

    @GetMapping(name="getStationByUid", path="stationByUid")
    public Mono<IServiceResult> getStationByUid(@RequestParam String arsId) {
        return wsBusGetStationByUidAPIService.getStationByUid(
                GetStationByUidAPIReqVo.builder()
                        .arsId(arsId)
                        .build()
        );
    }

    @GetMapping(name="getRouteByStation", path="routeByUid")
    public Mono<IServiceResult> getRouteByStation(@RequestParam String arsId) {
        return wsBusGetRouteByStationAPIService.getRouteByStation(
                GetRouteByStationAPIReqVo.builder()
                        .arsId(arsId)
                        .build()
        );
    }
}
