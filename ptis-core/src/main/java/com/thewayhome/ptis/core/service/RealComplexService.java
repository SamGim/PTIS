package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.entity.complex.RealComplex;
import com.thewayhome.ptis.core.repository.RealComplexRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RealComplexService {
    private final RealComplexRepository realComplexRepository;

    public RealComplexService(RealComplexRepository realComplexRepository) {
        this.realComplexRepository = realComplexRepository;
    }

    @Transactional
    public List<RealComplex> findAll() {
        return realComplexRepository.findAll();
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
