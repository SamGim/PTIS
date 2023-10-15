package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.BusRoute;
import com.thewayhome.ptis.vo.RouteFindingAPIReqVo;
import com.thewayhome.ptis.vo.Station;
import com.thewayhome.ptis.vo.tago.GetRouteInfoItemAPIReqVo;
import com.thewayhome.ptis.vo.wsbus.GetRouteByStationListAPINrmRespVoImpl;
import com.thewayhome.ptis.vo.wsbus.GetRouteByStationListAPIReqVo;
import com.thewayhome.ptis.vo.wsbus.GetStationsByPosListAPINrmRespVoImpl;
import com.thewayhome.ptis.vo.wsbus.GetStationsByPosListAPIReqVo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RouteFindingService {

    @Autowired
    private TagoBusGetRouteInfoIemAPIServiceImpl tagoBusGetRouteInfoIemAPIService;
    @Autowired
    private WsBusGetRouteByStationListAPIServiceImpl wsBusGetRouteByStationListAPIService;
    @Autowired
    private WsBusGetStationsByPosListAPIServiceImpl wsBusGetStationsByPosListAPIService;


    public Mono<String> findRoute(@Valid RouteFindingAPIReqVo req) {
        List<Station> stStatList = wsBusGetStationsByPosListAPIService
                .getStationsByPosList(
                        GetStationsByPosListAPIReqVo.builder()
                                .tmX(req.getStTmX())
                                .tmY(req.getStTmY())
                                .radius(req.getStRadius())
                                .build()
                )
                .map(x -> (GetStationsByPosListAPINrmRespVoImpl) x)
                .flatMapMany(x -> Flux.fromIterable(x.getMsgBody().getItemList()))
                .map(i -> Station.builder()
                        .arsId(i.getArsId())
                        .dist(i.getDist())
                        .gpsX(i.getGpsX())
                        .gpsY(i.getGpsY())
                        .posX(i.getPosX())
                        .posY(i.getPosY())
                        .stationId(i.getStationId())
                        .stationNm(i.getStationNm())
                        .stationTp(i.getStationTp())
                        .build()
                )
                .collectList()
                .block();

        List<Station> edStatList = wsBusGetStationsByPosListAPIService
                .getStationsByPosList(
                        GetStationsByPosListAPIReqVo.builder()
                                .tmX(req.getStTmX())
                                .tmY(req.getStTmY())
                                .radius(req.getStRadius())
                                .build()
                )
                .map(x -> (GetStationsByPosListAPINrmRespVoImpl) x)
                .flatMapMany(x -> Flux.fromIterable(x.getMsgBody().getItemList()))
                .map(i -> Station.builder()
                        .arsId(i.getArsId())
                        .dist(i.getDist())
                        .gpsX(i.getGpsX())
                        .gpsY(i.getGpsY())
                        .posX(i.getPosX())
                        .posY(i.getPosY())
                        .stationId(i.getStationId())
                        .stationNm(i.getStationNm())
                        .stationTp(i.getStationTp())
                        .build()
                )
                .collectList()
                .block();

        if (Objects.isNull(stStatList) || stStatList.isEmpty()) {
            throw new RuntimeException("출발지 주변 버스정류장을 찾을 수 없습니다.");
        }

        if (Objects.isNull(edStatList) || edStatList.isEmpty()) {
            throw new RuntimeException("도착지 주변 버스정류장을 찾을 수 없습니다.");
        }

        Station stStat = stStatList.get(0);
        Station edStat = edStatList.get(0);

        List<BusRoute> stBusRouteList = wsBusGetRouteByStationListAPIService.getRouteByStationList(
                        GetRouteByStationListAPIReqVo.builder()
                                .arsId(stStat.getArsId())
                                .build()
                )
                .map(x -> (GetRouteByStationListAPINrmRespVoImpl) x)
                .flatMapMany(x -> Flux.fromIterable(x.getMsgBody().getItemList()))
                .map(i -> BusRoute.builder()
                        .busRouteId(i.getBusRouteId())
                        .busRouteNm(i.getBusRouteNm())
                        .length(i.getLength())
                        .busRouteType(i.getBusRouteType())
                        .stBegin(i.getStBegin())
                        .stEnd(i.getStEnd())
                        .build()
                )
                .collectList()
                .block();

        tagoBusGetRouteInfoIemAPIService.getRouteInfoItem(
                        GetRouteInfoItemAPIReqVo.builder()
                                ._type("xml")
                                .cityCode("")
                                .routeId("")
                                .build()
                )
                .map(x -> (GetRouteByStationListAPINrmRespVoImpl) x)
                .flatMapMany(x -> Flux.fromIterable(x.getMsgBody().getItemList()))
                .map(i -> BusRoute.builder()
                        .busRouteId(i.getBusRouteId())
                        .busRouteNm(i.getBusRouteNm())
                        .length(i.getLength())
                        .busRouteType(i.getBusRouteType())
                        .stBegin(i.getStBegin())
                        .stEnd(i.getStEnd())
                        .build()
                )
                .collectList()
                .block();

        return Mono.just(req.toString());
    }
}
