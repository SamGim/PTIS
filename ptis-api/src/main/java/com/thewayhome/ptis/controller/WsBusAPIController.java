package com.thewayhome.ptis.controller;

import com.thewayhome.ptis.service.*;
import com.thewayhome.ptis.vo.wsbus.*;
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
    private WsBusGetStationByUidItemAPIServiceImpl wsBusGetStationByUidItemAPIService;
    @Autowired
    private WsBusGetRouteByStationListAPIServiceImpl wsBusGetRouteByStationListAPIService;
    @Autowired
    private WsBusGetBustimeByStationListAPIServiceImpl wsBusGetBustimeByStationListAPIService;
    @Autowired
    private WsBusGetStationsByPosListAPIServiceImpl wsBusGetStationsByPosListAPIService;
    @Autowired
    private WsBusGetStationsByRouteListAPIServiceImpl wsBusGetStationsByRouteListAPIService;
    @Autowired
    private WsBusGetRouteInfoItemAPIServiceImpl wsBusGetRouteInfoItemAPIService;
    @Autowired
    private WsBusGetRoutePathListAPIServiceImpl wsBusGetRoutePathListAPIService;
    @Autowired
    private WsBusGetBusRouteListAPIServiceImpl wsBusGetBusRouteListAPIService;


    @GetMapping(name="getStationByNameList", path="getStationByNameList")
    public Mono<IServiceResult> getStationByNameList(@RequestParam String stationName) {
        return wsBusGetStationByNameListAPIService.getStationByNameList(
                GetStationByNameListAPIReqVo.builder()
                        .stSrch(stationName)
                        .build()
        );
    }

    @GetMapping(name="getStationByUidItem", path="getStationByUidItem")
    public Mono<IServiceResult> getStationByUidItem(@RequestParam String arsId) {
        return wsBusGetStationByUidItemAPIService.getStationByUidItem(
                GetStationByUidItemAPIReqVo.builder()
                        .arsId(arsId)
                        .build()
        );
    }

    @GetMapping(name="getRouteByStationList", path="getRouteByStationList")
    public Mono<IServiceResult> getRouteByStationList(@RequestParam String arsId) {
        return wsBusGetRouteByStationListAPIService.getRouteByStationList(
                GetRouteByStationListAPIReqVo.builder()
                        .arsId(arsId)
                        .build()
        );
    }

    @GetMapping(name="getBustimeByStationList", path="getBustimeByStationList")
    public Mono<IServiceResult> getBustimeByStationList(
            @RequestParam String arsId,
            @RequestParam String busRouteId
    ) {
        return wsBusGetBustimeByStationListAPIService.getBustimeByStationList(
                GetBustimeByStationListAPIReqVo.builder()
                        .arsId(arsId)
                        .busRouteId(busRouteId)
                        .build()
        );
    }

    @GetMapping(name="getStationsByPosList", path="getStationsByPosList")
    public Mono<IServiceResult> getStationsByPosList(
            @RequestParam String tmX,
            @RequestParam String tmY,
            @RequestParam String radius
    ) {
        return wsBusGetStationsByPosListAPIService.getStationsByPosList(
                GetStationsByPosListAPIReqVo.builder()
                        .tmX(tmX)
                        .tmY(tmY)
                        .radius(radius)
                        .build()
        );
    }

    @GetMapping(name="getStationsByRouteList", path="getStationsByRouteList")
    public Mono<IServiceResult> getStationsByRouteList(
            @RequestParam String busRouteId
    ) {
        return wsBusGetStationsByRouteListAPIService.getStationsByRouteList(
                GetStationsByRouteListAPIReqVo.builder()
                        .busRouteId(busRouteId)
                        .build()
        );
    }

    @GetMapping(name="getRouteInfoItem", path="getRouteInfoItem")
    public Mono<IServiceResult> getRouteInfoItem(
            @RequestParam String busRouteId
    ) {
        return wsBusGetRouteInfoItemAPIService.getRouteInfoItem(
                GetRouteInfoItemAPIReqVo.builder()
                        .busRouteId(busRouteId)
                        .build()
        );
    }

    @GetMapping(name="getRoutePathList", path="getRoutePathList")
    public Mono<IServiceResult> getRoutePathList(
            @RequestParam String busRouteId
    ) {
        return wsBusGetRoutePathListAPIService.getRoutePathList(
                GetRoutePathListAPIReqVo.builder()
                        .busRouteId(busRouteId)
                        .build()
        );
    }



}
