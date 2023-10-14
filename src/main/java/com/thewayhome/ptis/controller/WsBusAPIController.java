package com.thewayhome.ptis.controller;

import com.thewayhome.ptis.service.WsBusAPIService;
import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@RestController
public class WsBusAPIController {

    @Autowired
    private WsBusAPIService wsBusAPIService;

    @GetMapping(name="getStationByNameList", path="stationByName")
    public Mono<IServiceResult> getStationByNameList(@RequestParam String stationName) {
        return wsBusAPIService.getStationByNameList(stationName);
    }
}
