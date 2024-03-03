package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.RealComplexRequestDto;
import com.thewayhome.ptis.core.dto.response.BusStationQueryResponseDto;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.entity.complex.RealComplex;
import com.thewayhome.ptis.core.repository.NodeRepository;
import com.thewayhome.ptis.core.repository.RealComplexRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RealComplexService {
    private final RealComplexRepository realComplexRepository;
    private final BatchService batchService;
    private final NodeRepository nodeRepository;

    public RealComplexService(RealComplexRepository realComplexRepository, BatchService batchService, NodeRepository nodeRepository) {
        this.realComplexRepository = realComplexRepository;
        this.batchService = batchService;

        this.nodeRepository = nodeRepository;
    }

    @Transactional
    public List<RealComplex> findAll() {
        return realComplexRepository.findAll();
    }

    public void saveRealComplex(RealComplexRequestDto realComplexRequestDto) {

        BusStationQueryResponseDto busStationQueryResponseDto = batchService.queryNearestBusStationInfo(realComplexRequestDto.getLatitude().toString(), realComplexRequestDto.getLongitude().toString(), "1000");
        Node node = nodeRepository.findByNodeSrcId(busStationQueryResponseDto.getBusStationId()).orElseThrow(() -> new IllegalArgumentException("해당 정류장 정보가 존재하지 않습니다."));
        RealComplex realComplex = RealComplex.builder()
                .id(realComplexRequestDto.getId())
                .name(realComplexRequestDto.getName())
                .latitude(realComplexRequestDto.getLatitude())
                .longitude(realComplexRequestDto.getLongitude())
                .nearestNodeId(node.getId())
                .nearestNodeTime(busStationQueryResponseDto.getDuration().longValue())
                .createdAt(LocalDateTime.now())
                .createdBy("S0001")
                .updatedAt(LocalDateTime.now())
                .updatedBy("S0001")
                .build();
        realComplexRepository.save(realComplex);
    }
  
    @Transactional
    public Optional<RealComplex> findById(Long id) {
        return realComplexRepository.findById(id);
    }

    @Transactional
    public void updateNearestNodeInfo(Long id, String nearestNodeId, Long nearestNodeTime) {
        Optional<RealComplex> realComplex = realComplexRepository.findById(id);
        if (realComplex.isPresent()) {
            realComplex.get().setNearestNodeId(nearestNodeId);
            realComplex.get().setNearestNodeTime(nearestNodeTime);
            realComplexRepository.save(realComplex.get());
        }
    }
}
