package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.repository.BusRouteProcessRepository;
import com.thewayhome.ptis.core.repository.BusRouteRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                busRouteProcessRepository.findByGatheringStatusCodeNot(gatheringStatusCode) :
                busRouteProcessRepository.findByGatheringStatusCode(gatheringStatusCode);
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

    public BusRoute saveBusRoute(BusRouteRegisterReqVo req) {
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

        return busRouteRepository.save(busRoute);
    }
}
