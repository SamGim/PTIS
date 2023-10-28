package com.thewayhome.ptis.api.service;

//import com.thewayhome.ptis.core.service.TagoBusGetRouteInfoIemAPIServiceImpl;
//import com.thewayhome.ptis.core.service.WsBusGetRouteByStationListAPIServiceImpl;
//import com.thewayhome.ptis.core.service.WsBusGetStationsByPosListAPIServiceImpl;
//import com.thewayhome.ptis.batch.vo.BusRoute;
//import com.thewayhome.ptis.batch.vo.BusStation;
//import com.thewayhome.ptis.core.vo.RouteFindingAPIReqVo;
//import com.thewayhome.ptis.core.vo.wsbus.GetRouteByStationListAPINrmRespVoImpl;
//import com.thewayhome.ptis.core.vo.wsbus.GetRouteByStationListAPIReqVo;
//import com.thewayhome.ptis.core.vo.wsbus.GetStationsByPosListAPINrmRespVoImpl;
//import com.thewayhome.ptis.core.vo.wsbus.GetStationsByPosListAPIReqVo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RouteFindingService {
//
//    @Autowired
//    private TagoBusGetRouteInfoIemAPIServiceImpl tagoBusGetRouteInfoIemAPIService;
//    @Autowired
//    private WsBusGetRouteByStationListAPIServiceImpl wsBusGetRouteByStationListAPIService;
//    @Autowired
//    private WsBusGetStationsByPosListAPIServiceImpl wsBusGetStationsByPosListAPIService;
//
//    private Mono<List<BusStation>> retrieveNearStation(String tmX, String tmY, String radius) {
//        return wsBusGetStationsByPosListAPIService
//                .getStationsByPosList(
//                        GetStationsByPosListAPIReqVo.builder()
//                                .tmX(tmX)
//                                .tmY(tmY)
//                                .radius(radius)
//                                .build()
//                )
//                .map(x -> (GetStationsByPosListAPINrmRespVoImpl) x)
//                .flatMapMany(x -> Flux.fromIterable(x.getMsgBody().getItemList()))
//                .map(i -> BusStation.builder()
//                        .arsId(i.getArsId())
//                        .dist(i.getDist())
//                        .gpsX(i.getGpsX())
//                        .gpsY(i.getGpsY())
//                        .posX(i.getPosX())
//                        .posY(i.getPosY())
//                        .stationId(i.getStationId())
//                        .stationNm(i.getStationNm())
//                        .stationTp(i.getStationTp())
//                        .build()
//                )
//                .collectList();
//    }
//
//    private Mono<List<BusRoute>> retrieveBusRoute(String arsId) {
//        return wsBusGetRouteByStationListAPIService.getRouteByStationList(
//                        GetRouteByStationListAPIReqVo.builder()
//                                .arsId(arsId)
//                                .build()
//                )
//                .map(x -> (GetRouteByStationListAPINrmRespVoImpl) x)
//                .flatMapMany(x -> Flux.fromIterable(x.getMsgBody().getItemList()))
//                .map(i -> BusRoute.builder()
//                        .busRouteId(i.getBusRouteId())
//                        .busRouteNm(i.getBusRouteNm())
//                        .length(i.getLength())
//                        .busRouteType(i.getBusRouteType())
//                        .stBegin(i.getStBegin())
//                        .stEnd(i.getStEnd())
//                        .build()
//                )
//                .collectList();
//    }
//
//    public Mono<String> findRoute(@Valid RouteFindingAPIReqVo req) {
//        List<BusStation> stStatList = retrieveNearStation(
//                req.getStTmX(),
//                req.getStTmY(),
//                req.getStRadius()
//        ).block();
//
//        if (Objects.isNull(stStatList) || stStatList.isEmpty()) {
//            throw new RuntimeException("출발지 주변 버스정류장을 찾을 수 없습니다.");
//        }
//
//        List<BusStation> edStatList = retrieveNearStation(
//                req.getEdTmX(),
//                req.getEdTmY(),
//                req.getEdRadius()
//        ).block();
//
//        if (Objects.isNull(edStatList) || edStatList.isEmpty()) {
//            throw new RuntimeException("도착지 주변 버스정류장을 찾을 수 없습니다.");
//        }
//
////        해당 노선 R 내 정류장 N1, N2, ...마다 탐색 로직 시작 (N = N1)
////        도착점 주변 정류장 E1, E2, ...마다 탐색 로직 시작 (E = E1)
////        N과 E가 일치하면 저장한다.
////        일치하지 않으면 다음 정류장으로 넘어간다. (E = E2, ...)
////        노선 R 내 다른 정류장으로 넘어간다. (N = N2, ...)
////        출발 정류장 S 내 다른 노선으로 넘어간다 (R = R2, ...)
////        출발점 주변 정류장 내 다른 정류장으로 넘어간다 (S = S2, ...)
//        Stack<BusStation> rtFdStk = new Stack<>();
//
////        for (int stStatIdx = 0; stStatIdx < stStatList.size(); stStatIdx++) {
////            Station stStat = stStatList.get(stStatIdx);
////            rtFdStk.push(stStat);
////
////            List<BusRoute> stBusRouteList = retrieveBusRoute(stStat.getArsId()).block();
////
////            if (Objects.isNull(stBusRouteList) || stBusRouteList.isEmpty()) {
////                throw new RuntimeException("출발 정류장의 버스 노선을 찾을 수 없습니다.");
////            }
////
////            for (int stBusRoutIdx = 0; stBusRoutIdx < stBusRouteList.size(); stBusRoutIdx++) {
////                BusRoute stBusRoute = stBusRouteList.get(stBusRoutIdx);
////
////                BusRoute curBusRoute = stBusRoute;
////                for (int curStatIdx = 0; curStatIdx = curBusRoute.getBusRouteLength(); curStatIdx++) {
////                    Station curStat = curBusRoute.getStList.get(curStatIdx);
////                    rtFdStk.push(curStat);
////
////                    for (int edStatIdx = 0; edStatIdx < edStatList.size(); edStatIdx++) {
////                        Station edStat = edStatList.get(edStatIdx);
////                        List<BusRoute> edBusRouteList = retrieveBusRoute(edStat.getArsId()).block();
////                    }
////                }
////            }
////
////            rtFdStk.pop();
////        }
//
//
//
////        tagoBusGetRouteInfoIemAPIService.getRouteInfoItem(
////                        GetRouteInfoItemAPIReqVo.builder()
////                                ._type("xml")
////                                .cityCode("")
////                                .routeId("")
////                                .build()
////                )
////                .map(x -> (GetRouteByStationListAPINrmRespVoImpl) x)
////                .flatMapMany(x -> Flux.fromIterable(x.getMsgBody().getItemList()))
////                .map(i -> BusRoute.builder()
////                        .busRouteId(i.getBusRouteId())
////                        .busRouteNm(i.getBusRouteNm())
////                        .length(i.getLength())
////                        .busRouteType(i.getBusRouteType())
////                        .stBegin(i.getStBegin())
////                        .stEnd(i.getStEnd())
////                        .build()
////                )
////                .collectList()
////                .block();
//
//        return Mono.just(req.toString());
//    }
}
