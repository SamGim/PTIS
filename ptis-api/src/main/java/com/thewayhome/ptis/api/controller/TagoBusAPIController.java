package com.thewayhome.ptis.api.controller;

import com.thewayhome.ptis.api.service.TagoBusGetRouteInfoIemAPIServiceImpl;
import com.thewayhome.ptis.api.vo.tago.GetRouteInfoItemAPIReqVo;
import com.thewayhome.ptis.api.vo.tago.IResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tago")
public class TagoBusAPIController {
    @Autowired
    private TagoBusGetRouteInfoIemAPIServiceImpl tagoBusGetRouteInfoIemAPIService;

    @GetMapping(name="getRouteInfoItem", path="getRouteInfoItem")
    public Mono<IResponse> getRouteInfoItem(
            @RequestParam String cityCode,
            @RequestParam String routeId
    ) {
        return tagoBusGetRouteInfoIemAPIService.getRouteInfoItem(
                GetRouteInfoItemAPIReqVo.builder()
                        ._type("xml")
                        .cityCode(cityCode)
                        .routeId(routeId)
                        .build()
        );
    }
}