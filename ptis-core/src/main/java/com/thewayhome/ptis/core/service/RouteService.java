package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Route;
import com.thewayhome.ptis.core.entity.Station;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.RouteRepository;
import com.thewayhome.ptis.core.repository.StationRepository;
import com.thewayhome.ptis.core.vo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

    @Transactional
    public Route saveRoute(RouteRegisterReqVo req) {
        Route route = new Route();

        // COM
        route.setTpDscd("B");
        route.setSrDscd("R");
        route.setGsDscd(" ");

        // ID
        IdSequence idSequence = idSequenceRepository.findById("ROUTE")
                .orElse(new IdSequence("ROUTE", 0L));
        Long nextId = idSequence.getNextId();

        idSequence.setNextId(nextId + 1);
        idSequenceRepository.save(idSequence);

        route.setId(String.format("%012d", nextId + 1));

        // DATA
        route.setCostBase(req.getCostBase());
        route.setCostSub(req.getCostSub());
        route.setCostAdd(req.getCostAdd());

        Optional<Station> stationSt = stationRepository.findById(req.getStationStId());
        Optional<Station> stationEd = stationRepository.findById(req.getStationEdId());

        stationSt.ifPresent(route::setStationSt);
        stationEd.ifPresent(route::setStationEd);

        // DB
        if (routeRepository.findById(route.getId()).isEmpty()) {
            route.setCreatedAt(LocalDateTime.now());
            route.setCreatedBy(req.getOperatorId());
        }
        route.setUpdatedAt(LocalDateTime.now());
        route.setUpdatedBy(req.getOperatorId());

        return routeRepository.save(route);
    }
}
