package com.thewayhome.ptis.controller;

import com.thewayhome.ptis.service.WsBusAPIService;
import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WsBusAPIController {

    @Autowired
    private WsBusAPIService wsBusAPIService;

    @GetMapping(name="getStationByNameList", path="stationByName")
    public Mono<IServiceResult> getStationByNameList(@RequestParam String stationName) {
        return wsBusAPIService.getStationByNameList(stationName);
    }

    @GetMapping(name="getStationByUid", path="stationByUid")
    public Mono<IServiceResult> getStationByUid(@RequestParam String arsId) {
        return wsBusAPIService.getStationByUid(arsId);
    }

    @GetMapping(name="getRouteByStation", path="routeByUid")
    public Mono<IServiceResult> getRouteByStation(@RequestParam String arsId) {
        return wsBusAPIService.getRouteByStation(arsId);
    }
}
