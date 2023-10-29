package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.repository.BusRouteRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.vo.BusRoute;
import com.thewayhome.ptis.core.vo.BusRouteRegisterReqVo;
import com.thewayhome.ptis.core.vo.IdSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BusRouteService {
    @Autowired
    private BusRouteRepository busRouteRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

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
