package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.RealComplexRequestDto;
import com.thewayhome.ptis.core.entity.complex.RealComplex;
import com.thewayhome.ptis.core.repository.RealComplexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealComplexService {
    private final RealComplexRepository realComplexRepository;

    public RealComplexService(RealComplexRepository realComplexRepository) {
        this.realComplexRepository = realComplexRepository;
    }

    public List<RealComplex> findAll() {
        return realComplexRepository.findAll();
    }

    public void saveRealComplex(RealComplexRequestDto realComplexRequestDto) {
        RealComplex realComplex = RealComplex.builder()
                .id(realComplexRequestDto.getId())
                .name(realComplexRequestDto.getName())
                .latitude(realComplexRequestDto.getLatitude())
                .longitude(realComplexRequestDto.getLongitude())
                .build();
        realComplexRepository.save(realComplex);
    }
}
