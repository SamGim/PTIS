package com.thewayhome.ptis.controller;

import com.thewayhome.ptis.service.MainService;
import com.thewayhome.ptis.vo.IServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@RestController
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping(name="getStationByNameList", path="station")
    public Mono<IServiceResult> getStationByNameList(@RequestParam String stationName) {
        try {
            return mainService.getStationByNameList(stationName);
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
