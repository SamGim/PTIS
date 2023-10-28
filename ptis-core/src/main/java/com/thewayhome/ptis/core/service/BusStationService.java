package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.repository.BusStationRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.vo.BusStation;
import com.thewayhome.ptis.core.vo.BusStationRegisterReqVo;
import com.thewayhome.ptis.core.vo.IdSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusStationService {
    @Autowired
    private BusStationRepository busStationRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

    public List<BusStation> getAllBusStation() {
        return busStationRepository.findAll();
    }

    public BusStation saveBusStation(BusStationRegisterReqVo req) {
        BusStation busStation = busStationRepository.findByBusStationId(req.getBusStationId()).orElse(new BusStation());

        // TYPE
        busStation.setTpDscd("B");
        busStation.setSrDscd("S");
        busStation.setGsDscd("W");

        // ID
        if (busStation.getId() == null) {
            IdSequence idSequence = idSequenceRepository.findById("BUS_STATION")
                    .orElse(new IdSequence("BUS_STATION", 0L));
            Long nextId = idSequence.getNextId();

            idSequence.setNextId(nextId + 1);
            idSequenceRepository.save(idSequence);

            busStation.setId(String.format("%012d", nextId + 1));

            busStation.setCreatedAt(LocalDateTime.now());
            busStation.setCreatedBy(req.getOperatorId());
        }

        // DATA
        busStation.setBusStationId(req.getBusStationId());
        busStation.setBusStationNo(req.getBusStationNo());
        busStation.setBusStationName(req.getBusStationName());
        busStation.setBusStationPosX(Double.parseDouble(req.getBusStationPosX()));
        busStation.setBusStationPosY(Double.parseDouble(req.getBusStationPosY()));

        // DB
        busStation.setUpdatedAt(LocalDateTime.now());
        busStation.setUpdatedBy(req.getOperatorId());

        return busStationRepository.save(busStation);
    }
}
