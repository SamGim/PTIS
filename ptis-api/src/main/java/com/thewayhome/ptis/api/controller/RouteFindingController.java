package com.thewayhome.ptis.api.controller;

import com.thewayhome.ptis.api.vo.RouteFindingAPIReqVo;
import com.thewayhome.ptis.api.service.RouteFindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RouteFindingController {
//    @Autowired
//    private RouteFindingService routeFindingService;
//
//    @GetMapping(name="findRoute", path="findRoute")
//    public Mono<String> findRoute(
//            @RequestParam String stTmX,
//            @RequestParam String stTmY,
//            @RequestParam String stRadius,
//            @RequestParam String edTmX,
//            @RequestParam String edTmY,
//            @RequestParam String edRadius
//    ) {
//        return routeFindingService.findRoute(
//                RouteFindingAPIReqVo.builder()
//                        .stTmX(stTmX)
//                        .stTmY(stTmY)
//                        .stRadius(stRadius)
//                        .edTmX(edTmX)
//                        .edTmY(edTmY)
//                        .edRadius(edRadius)
//                        .build()
//        );
//    }
}