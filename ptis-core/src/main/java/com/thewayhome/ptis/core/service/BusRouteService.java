package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.entity.BusRoute;
import com.thewayhome.ptis.core.entity.BusRouteProcess;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.repository.BusRouteProcessRepository;
import com.thewayhome.ptis.core.repository.BusRouteRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BusRouteService {
    @Autowired
    private BusRouteRepository busRouteRepository;
    @Autowired
    private BusRouteProcessRepository busRouteProcessRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

    public List<BusRoute> findBusRouteByGatheringStatusCode(String gatheringStatusCode, boolean notCondition) {
        List<BusRouteProcess> busRouteProcess = notCondition ?
                busRouteProcessRepository.findByGatheringStatusCodeNotOrderById(gatheringStatusCode) :
                busRouteProcessRepository.findByGatheringStatusCodeOrderById(gatheringStatusCode);
        return busRouteProcess != null ? busRouteProcess.stream().map(BusRouteProcess::getBusRoute).toList() : null;
    }

    public List<BusRoute> findBusRouteByFirstGatheringDate(String startDate, String endDate) {
        List<BusRouteProcess> busRouteProcess = busRouteProcessRepository.findByFirstGatheringDateInDateRange(startDate, endDate);
        return busRouteProcess != null ? busRouteProcess.stream().map(BusRouteProcess::getBusRoute).toList() : null;
    }

    public List<BusRoute> findBusRouteByLastGatheringDate(String startDate, String endDate) {
        List<BusRouteProcess> busRouteProcess = busRouteProcessRepository.findByLastGatheringDateInDateRange(startDate, endDate);
        return busRouteProcess != null ? busRouteProcess.stream().map(BusRouteProcess::getBusRoute).toList() : null;
    }

    public BusRoute saveBusRoute(BusRouteRegisterReqDto req) {
        BusRoute busRoute = busRouteRepository.findByBusRouteId(req.getBusRouteId()).orElse(new BusRoute());

        // TYPE
        busRoute.setTpDscd("B");
        busRoute.setSrDscd("R");
        busRoute.setGsDscd("W");

        // ID
        if (busRoute.getId() == null) {
            IdSequence idSequence = idSequenceRepository.findById("BUS_ROUTE")
                    .orElse(new IdSequence("BUS_ROUTE", 0L));
            Long nextId = idSequence.getNextId();

            idSequence.setNextId(nextId + 1);
            idSequenceRepository.save(idSequence);

            busRoute.setId(String.format("%012d", nextId + 1));

            busRoute.setCreatedAt(LocalDateTime.now());
            busRoute.setCreatedBy(req.getOperatorId());
        }

        // DATA
        busRoute.setBusRouteId(req.getBusRouteId());
        busRoute.setBusRouteName(req.getBusRouteName());
        busRoute.setBusRouteNo(req.getBusRouteNo());
        busRoute.setBusRouteSubNo(req.getBusRouteSubNo());

        // DB
        busRoute.setUpdatedAt(LocalDateTime.now());
        busRoute.setUpdatedBy(req.getOperatorId());

        BusRouteProcess busRouteProcess = busRouteProcessRepository.findById(busRoute.getId()).orElse(new BusRouteProcess());

        // DB
        if (busRouteProcess.getId() == null) {
            busRouteProcess.setCreatedAt(LocalDateTime.now());
            busRouteProcess.setCreatedBy(req.getOperatorId());
            busRouteProcess.setFirstGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            busRouteProcess.setGatheringStatusCode("01");
        }
        busRouteProcess.setUpdatedAt(LocalDateTime.now());
        busRouteProcess.setUpdatedBy(req.getOperatorId());
        busRouteProcess.setLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // ID
        busRouteProcess.setId(busRoute.getId());

        busRouteProcessRepository.save(busRouteProcess);

        return busRouteRepository.save(busRoute);
    }

    public void changeBusRouteGatheringStatusCode(BusRouteProcessRegisterReqDto req) {
        // ID
        BusRouteProcess busRouteProcess = busRouteProcessRepository.findById(req.getId()).orElseThrow(IllegalStateException::new);

        // DATA
        busRouteProcess.setGatheringStatusCode(req.getGatheringStatusCode());

        // DB
        busRouteProcess.setUpdatedAt(LocalDateTime.now());
        busRouteProcess.setUpdatedBy(req.getOperatorId());

        busRouteProcessRepository.save(busRouteProcess);
    }
}
