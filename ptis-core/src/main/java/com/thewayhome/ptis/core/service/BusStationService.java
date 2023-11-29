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
    private final BusStationEntityVoConverter busStationEntityDtoConverter;
    private final BusStationProcessEntityVoConverter busStationProcessEntityDtoConverter;


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

    public BusStation saveBusStation(BusStationVo req) {
        BusStation entity = busStationEntityDtoConverter.toEntity(req, req.getOperatorId());
        return busStationRepository.save(entity);
    }

    public BusStationProcess saveBusStationProcess(BusStationProcessVo req) {
        BusStationProcess entity = busStationProcessEntityDtoConverter.toEntity(req, req.getOperatorId());
        return busStationProcessRepository.save(entity);
    }

    public BusStationVo registerBusStation(BusStationRegisterRequestDto req) {
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
                .operatorId(req.getOperatorId())
                .build();

        BusStation busStation = this.saveBusStation(busStationVo);

        return busStationEntityDtoConverter.toVo(busStation, req.getOperatorId());
    }

    public BusStationProcessVo registerBusStationProcess(BusStationProcessRegisterRequestDto req) {
        // BusStation
        BusStationVo busStationVo = BusStationVo.builder()
                .id(req.getId())
                .operatorId(req.getOperatorId())
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
                .operatorId(req.getOperatorId())
                .build();

        BusStationProcess busStationProcess = this.saveBusStationProcess(busStationProcessVo);

        return busStationProcessEntityDtoConverter.toVo(busStationProcess, req.getOperatorId());
    }

    public BusStationVo updateBusStationDetail(BusStationRegisterRequestDto req) {
        BusStation busStation = this.findById(req.getId()).orElseThrow(IllegalArgumentException::new);

        BusStationProcessVo busStationProcessVo = BusStationProcessVo.builder()
                .id(req.getId())
                .busStationLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busStationGatheringStatusCode("02")
                .operatorId(req.getOperatorId())
                .build();

        this.saveBusStationProcess(busStationProcessVo);

        return busStationEntityDtoConverter.toVo(busStation, req.getOperatorId());
    }

    public BusStationProcessVo updateBusRoutesGatheringStatusCode(BusStationProcessRegisterRequestDto req) {
        BusStation busStation = this.findById(req.getId()).orElseThrow(IllegalArgumentException::new);

        BusStationProcessVo busStationProcessVo = BusStationProcessVo.builder()
                .id(req.getId())
                .busStation(busStationEntityDtoConverter.toVo(busStation, req.getOperatorId()))
                .busRouteLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .busRouteGatheringStatusCode("01")
                .operatorId(req.getOperatorId())
                .build();

        BusStationProcess busStationProcess = this.saveBusStationProcess(busStationProcessVo);

        return busStationProcessEntityDtoConverter.toVo(busStationProcess, req.getOperatorId());
    }

    public List<BusStation> findAll() {
        return busStationRepository.findAll();
    }
}
