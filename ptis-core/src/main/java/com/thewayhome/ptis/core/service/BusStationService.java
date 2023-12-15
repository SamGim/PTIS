package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.BusStationProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterRequestDto;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.BusStationProcess;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.repository.BusStationProcessRepository;
import com.thewayhome.ptis.core.repository.BusStationRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.util.BusStationEntityVoConverter;
import com.thewayhome.ptis.core.util.BusStationProcessEntityVoConverter;
import com.thewayhome.ptis.core.vo.BusStationProcessVo;
import com.thewayhome.ptis.core.vo.BusStationVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusStationService {
    private final BusStationRepository busStationRepository;
    private final BusStationProcessRepository busStationProcessRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final BusStationEntityVoConverter busStationEntityVoConverter;
    private final BusStationProcessEntityVoConverter busStationProcessEntityVoConverter;


    public List<BusStation> getAllBusStation() {
        return busStationRepository.findAll();
    }

    public Optional<BusStation> findById(String id) {
        return busStationRepository.findById(id);
    }
    public Optional<BusStationProcess> findProcessById(String id) {
        return busStationProcessRepository.findById(id);
    }

    public Optional<BusStation> findByArsId(String arsId) {
        return busStationRepository.findByBusStationId(arsId);
    }
    public List<BusStation> findBusStationByBusRouteGatheringStatusCode(String gatheringStatusCode, boolean notCondition) {
        List<BusStationProcess> busStationProcess = notCondition ?
                busStationProcessRepository.findByBusRouteGatheringStatusCodeNotOrderById(gatheringStatusCode) :
                busStationProcessRepository.findByBusRouteGatheringStatusCodeOrderById(gatheringStatusCode);
        return busStationProcess != null ? busStationProcess.stream().map(BusStationProcess::getBusStation).toList() : null;
    }
    public List<BusStation> findBusStationByNodeCreationStatusCode(String nodeCreationStatusCode, boolean notCondition) {
        List<BusStationProcess> busStationProcess = notCondition ?
                busStationProcessRepository.findByNodeCreationStatusCodeNotOrderById(nodeCreationStatusCode) :
                busStationProcessRepository.findByNodeCreationStatusCodeOrderById(nodeCreationStatusCode);
        return busStationProcess != null ? busStationProcess.stream().map(BusStationProcess::getBusStation).toList() : null;
    }

    public BusStation saveBusStation(BusStationVo req, String operatorId) {
        BusStation entity = busStationEntityVoConverter.toEntity(req, operatorId);
        return busStationRepository.save(entity);
    }

    public BusStationProcess saveBusStationProcess(BusStationProcessVo req, String operatorId) {
        BusStationProcess entity = busStationProcessEntityVoConverter.toEntity(req, operatorId);
        return busStationProcessRepository.save(entity);
    }

    public BusStationVo registerBusStation(BusStationRegisterRequestDto req) {
        Optional<BusStation> byArsId = this.findByArsId(req.getBusStationId());

        if (byArsId.isPresent()) {
            return busStationEntityVoConverter.toVo(byArsId.get());
        } else {
            // ID
            IdSequence idSequence = idSequenceRepository.findById("BUS_STATION")
                    .orElse(new IdSequence("BUS_STATION", 0L));
            Long id = idSequence.getNextId() + 1;

            idSequence.setNextId(id);
            idSequenceRepository.save(idSequence);

            req.setId(String.format("%012d", id));

            // BusStation
            BusStationVo busStationVo = BusStationVo.builder()
                    .id(req.getId())
                    .busStationId(req.getBusStationId())
                    .busStationName(req.getBusStationName())
                    .busStationNo(req.getBusStationNo())
                    .busStationPosX(Double.valueOf(req.getBusStationPosX()))
                    .busStationPosY(Double.valueOf(req.getBusStationPosY()))
                    .build();

            BusStation busStation = this.saveBusStation(busStationVo, req.getOperatorId());

            return busStationEntityVoConverter.toVo(busStation);
        }
    }

    public BusStationProcessVo registerBusStationProcess(BusStationProcessRegisterRequestDto req) {
        // BusStation
        BusStationVo busStationVo = BusStationVo.builder()
                .id(req.getId())
                .build();

        // BusStationProcess
        BusStationProcessVo busStationProcessVo = BusStationProcessVo.builder()
                .id(req.getId())
                .busStation(busStationVo)
                .busRouteLastGatheringDate(req.getBusRouteLastGatheringDate())
                .busRouteGatheringStatusCode(req.getBusRouteGatheringStatusCode())
                .busStationLastGatheringDate(req.getBusStationLastGatheringDate())
                .busStationGatheringStatusCode(req.getBusStationGatheringStatusCode())
                .nodeLastCreationDate(req.getNodeLastCreationDate())
                .nodeCreationStatusCode(req.getNodeCreationStatusCode())
                .build();

        BusStationProcess busStationProcess = this.saveBusStationProcess(busStationProcessVo, req.getOperatorId());

        return busStationProcessEntityVoConverter.toVo(busStationProcess);
    }

    public BusStationVo updateBusStationDetail(BusStationRegisterRequestDto req) {
        BusStation busStation = this.findById(req.getId()).orElseThrow(IllegalArgumentException::new);

        BusStationProcessVo busStationProcessVo = BusStationProcessVo.builder()
                .id(req.getId())
                .busStationLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busStationGatheringStatusCode("02")
                .build();

        this.saveBusStationProcess(busStationProcessVo, req.getOperatorId());

        return busStationEntityVoConverter.toVo(busStation);
    }

    public BusStationProcessVo updateBusRoutesGatheringStatusCode(BusStationProcessRegisterRequestDto req) {
        BusStation busStation = this.findById(req.getId()).orElseThrow(IllegalArgumentException::new);

        BusStationProcessVo busStationProcessVo = BusStationProcessVo.builder()
                .id(req.getId())
                .busStation(busStationEntityVoConverter.toVo(busStation))
                .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busRouteGatheringStatusCode(req.getBusRouteGatheringStatusCode())
                .build();

        BusStationProcess busStationProcess = this.saveBusStationProcess(busStationProcessVo, req.getOperatorId());

        return busStationProcessEntityVoConverter.toVo(busStationProcess);
    }

    public List<BusStation> findAll() {
        return busStationRepository.findAll();
    }
}
