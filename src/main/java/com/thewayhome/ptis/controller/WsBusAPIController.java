package com.thewayhome.ptis.controller;

import com.thewayhome.ptis.service.WsBusAPIService;
import com.thewayhome.ptis.vo.wsbus.getstationbynamelist.IServiceResult;
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

    @GetMapping(name="getStationByNameList", path="station")
    public Mono<IServiceResult> getStationByNameList(@RequestParam String stationName) {
        try {
            return wsBusAPIService.getStationByNameList(stationName);
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
