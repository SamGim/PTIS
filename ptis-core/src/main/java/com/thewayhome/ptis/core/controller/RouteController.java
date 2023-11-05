package com.thewayhome.ptis.core.controller;

import com.thewayhome.ptis.core.service.RouteService;
import com.thewayhome.ptis.core.entity.Route;
import com.thewayhome.ptis.core.dto.RouteRegisterReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {
    @Autowired
    private RouteService routeService;

    @PostMapping(name="/route", path="/route", produces = MediaType.APPLICATION_JSON_VALUE)
    public Route createRoute(@RequestBody RouteRegisterReqDto req) {
        return routeService.saveRoute(req);
    }
}
