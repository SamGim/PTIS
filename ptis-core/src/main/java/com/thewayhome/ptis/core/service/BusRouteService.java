package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.vo.BusRouteVo;
import com.thewayhome.ptis.core.vo.BusRouteProcessVo;
import com.thewayhome.ptis.core.dto.request.BusRouteProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusRouteRegisterRequestDto;
import com.thewayhome.ptis.core.entity.BusRoute;
import com.thewayhome.ptis.core.entity.BusRouteProcess;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.repository.BusRouteProcessRepository;
import com.thewayhome.ptis.core.repository.BusRouteRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.util.BusRouteEntityVoConverter;
import com.thewayhome.ptis.core.util.BusRouteProcessEntityVoConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusRouteService {
    private final BusRouteRepository busRouteRepository;
    private final BusRouteProcessRepository busRouteProcessRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final BusRouteEntityVoConverter busRouteEntityDtoConverter;
    private final BusRouteProcessEntityVoConverter busRouteProcessEntityDtoConverter;

    public Optional<BusRoute> findById(String id) {
        return busRouteRepository.findById(id);
    }
    public Optional<BusRouteProcess> findProcessById(String id) {
        return busRouteProcessRepository.findById(id);
    }
    public Optional<BusRoute> findByBusRouteId(String busRouteId) {
        return busRouteRepository.findByBusRouteId(busRouteId);
    }

    public List<BusRoute> findBusRouteByGatheringStatusCode(String gatheringStatusCode, boolean notCondition) {
        List<BusRouteProcess> busRouteProcess = notCondition ?
                busRouteProcessRepository.findByBusStationGatheringStatusCodeNotOrderById(gatheringStatusCode) :
                busRouteProcessRepository.findByBusStationGatheringStatusCodeOrderById(gatheringStatusCode);
        return busRouteProcess != null ? busRouteProcess.stream().map(BusRouteProcess::getBusRoute).toList() : null;
    }

    private BusRoute saveBusRoute(BusRouteVo req) {
        BusRoute entity = busRouteEntityDtoConverter.toEntity(req, req.getOperatorId());
        return busRouteRepository.save(entity);
    }

    private BusRouteProcess saveBusRouteProcess(BusRouteProcessVo req) {
        BusRouteProcess entity = busRouteProcessEntityDtoConverter.toEntity(req, req.getOperatorId());
        return busRouteProcessRepository.save(entity);
    }

    @Transactional
    public BusRoute registerBusRoute(BusRouteRegisterRequestDto req) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("BUS_ROUTE")
                .orElse(new IdSequence("BUS_ROUTE", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        // BusRoute
        BusRouteVo busRouteVo = BusRouteVo.builder()
                .id(req.getId())
                .busRouteId(req.getBusRouteId())
                .busRouteName(req.getBusRouteName())
                .busRouteNo(req.getBusRouteNo())
                .busRouteSubNo(req.getBusRouteSubNo())
                .busStationSt(req.getBusStationSt())
                .busStationEd(req.getBusStationEd())
                .operatorId(req.getOperatorId())
                .build();

        BusRoute busRoute = this.saveBusRoute(busRouteVo);

        // BusRouteProcess
        BusRouteProcessVo busRouteProcessVo = BusRouteProcessVo.builder()
                .id(req.getId())
                .busRoute(busRouteVo)
                .busRouteFirstGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busRouteGatheringStatusCode("01")
                .operatorId(req.getOperatorId())
                .build();

        this.saveBusRouteProcess(busRouteProcessVo);

        return busRoute;
    }

    @Transactional
    public BusRoute updateBusRouteDetail(BusRouteRegisterRequestDto req) {
        BusRouteVo busRouteVo = BusRouteVo.builder()
                .id(req.getId())
                .busRouteId(req.getBusRouteId())
                .busRouteName(req.getBusRouteName())
                .busRouteNo(req.getBusRouteNo())
                .busRouteSubNo(req.getBusRouteSubNo())
                .busStationSt(req.getBusStationSt())
                .busStationEd(req.getBusStationEd())
                .operatorId(req.getOperatorId())
                .build();

        BusRoute busRoute = this.saveBusRoute(busRouteVo);

        BusRouteProcessVo busRouteProcessVo = BusRouteProcessVo.builder()
                .id(req.getId())
                .busRoute(busRouteVo)
                .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busRouteGatheringStatusCode("02")
                .operatorId(req.getOperatorId())
                .build();

        this.saveBusRouteProcess(busRouteProcessVo);

        return busRoute;
    }

    @Transactional
    public BusRoute updateBusStationsGatheringStatusCode(BusRouteProcessRegisterRequestDto req) {
        BusRoute busRoute = this.findById(req.getId()).orElseThrow(IllegalArgumentException::new);

        BusRouteProcessVo busRouteProcessVo = BusRouteProcessVo.builder()
                .id(req.getId())
                .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busRouteGatheringStatusCode("02")
                .operatorId(req.getOperatorId())
                .build();

        this.saveBusRouteProcess(busRouteProcessVo);

        return busRoute;
    }
}
