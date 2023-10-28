package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.StationRepository;
import com.thewayhome.ptis.core.vo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class StationService {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

    @Transactional
    public Station saveStation(StationRegisterReqVo req) {
        Station station = new Station();

        // COM
        station.setTpDscd("B");
        station.setSrDscd("S");
        station.setGsDscd(" ");

        // ID
        IdSequence idSequence = idSequenceRepository.findById("STATION")
                .orElse(new IdSequence("STATION", 0L));
        Long nextId = idSequence.getNextId();

        idSequence.setNextId(nextId + 1);
        idSequenceRepository.save(idSequence);

        station.setId(String.format("%012d", nextId + 1));

        // DATA
        station.setStationId(req.getStationId());
        station.setStationPosX(Double.parseDouble(req.getStationPosX()));
        station.setStationPosY(Double.parseDouble(req.getStationPosY()));

        // DB
        if (stationRepository.findById(station.getId()).isEmpty()) {
            station.setCreatedAt(LocalDateTime.now());
            station.setCreatedBy(req.getOperatorId());
        }
        station.setUpdatedAt(LocalDateTime.now());
        station.setUpdatedBy(req.getOperatorId());

        return stationRepository.save(station);
    }
}
