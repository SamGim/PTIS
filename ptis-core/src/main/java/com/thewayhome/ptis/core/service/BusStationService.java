package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.repository.BusStationProcessRepository;
import com.thewayhome.ptis.core.repository.BusStationRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BusStationService {
    @Autowired
    private BusStationRepository busStationRepository;
    @Autowired
    private BusStationProcessRepository busStationProcessRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

    public List<BusStation> getAllBusStation() {
        return busStationRepository.findAll();
    }


    public Optional<BusStation> findByArsId(String arsId) {
        return busStationRepository.findByBusStationId(arsId);
    }
    public List<BusStation> findBusStationByGatheringStatusCode(String gatheringStatusCode, boolean notCondition) {
        List<BusStationProcess> busStationProcess = notCondition ?
                busStationProcessRepository.findByGatheringStatusCodeNot(gatheringStatusCode) :
                busStationProcessRepository.findByGatheringStatusCode(gatheringStatusCode);
        return busStationProcess != null ? busStationProcess.stream().map(BusStationProcess::getBusStation).toList() : null;
    }

    public List<BusStation> findBusStationByFirstGatheringDate(String startDate, String endDate) {
        List<BusStationProcess> busStationProcess = busStationProcessRepository.findByFirstGatheringDateInDateRange(startDate, endDate);
        return busStationProcess != null ? busStationProcess.stream().map(BusStationProcess::getBusStation).toList() : null;
    }

    public List<BusStation> findBusStationByLastGatheringDate(String startDate, String endDate) {
        List<BusStationProcess> busStationProcess = busStationProcessRepository.findByLastGatheringDateInDateRange(startDate, endDate);
        return busStationProcess != null ? busStationProcess.stream().map(BusStationProcess::getBusStation).toList() : null;
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

        BusStationProcess busStationProcess = busStationProcessRepository.findById(busStation.getId()).orElse(new BusStationProcess());

        // DB
        if (busStationProcess.getId() == null) {
            busStationProcess.setCreatedAt(LocalDateTime.now());
            busStationProcess.setCreatedBy(req.getOperatorId());
            busStationProcess.setFirstGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }
        busStationProcess.setUpdatedAt(LocalDateTime.now());
        busStationProcess.setUpdatedBy(req.getOperatorId());
        busStationProcess.setLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // ID
        busStationProcess.setId(busStation.getId());

        // DATA
        busStationProcess.setGatheringStatusCode("01");

        busStationProcessRepository.save(busStationProcess);

        return busStationRepository.save(busStation);
    }

    public void changeBusStationGatheringStatusCode(BusStationProcessRegisterReqVo req) {
        // ID
        BusStationProcess busStationProcess = busStationProcessRepository.findById(req.getId()).orElseThrow(IllegalStateException::new);

        // DATA
        busStationProcess.setGatheringStatusCode(req.getGatheringStatusCode());

        // DB
        busStationProcess.setUpdatedAt(LocalDateTime.now());
        busStationProcess.setUpdatedBy(req.getOperatorId());

        busStationProcessRepository.save(busStationProcess);
    }
}
