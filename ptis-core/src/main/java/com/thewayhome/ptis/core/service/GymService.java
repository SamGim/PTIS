package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.GymRegisterProcessRequestDto;
import com.thewayhome.ptis.core.dto.request.GymRegisterRequestDto;
import com.thewayhome.ptis.core.entity.Gym;
import com.thewayhome.ptis.core.entity.GymProcess;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.repository.GymProcessRepository;
import com.thewayhome.ptis.core.repository.GymRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.util.GymEntityVoConverter;
import com.thewayhome.ptis.core.util.GymProcessEntityVoConverter;
import com.thewayhome.ptis.core.vo.GymProcessVo;
import com.thewayhome.ptis.core.vo.GymVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymService {
    private final GymRepository gymRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final GymProcessRepository gymProcessRepository;
    private final GymEntityVoConverter gymEntityDtoConverter;
    private final GymProcessEntityVoConverter gymProcessEntityDtoConverter;

    public Gym saveGym(GymVo req, String operatorId) {
        Gym entity = gymEntityDtoConverter.toEntity(req, operatorId);
        return gymRepository.save(entity);
    }

    public GymProcess saveGymProcess(GymProcessVo req, String operatorId) {
        GymProcess entity = gymProcessEntityDtoConverter.toEntity(req, operatorId);
        return gymProcessRepository.save(entity);
    }

    public GymVo registerGym(GymRegisterRequestDto req) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("BUS_STATION")
                .orElse(new IdSequence("BUS_STATION", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        // Gym
        GymVo gymVo = GymVo.builder()
                .id(req.getId())
                .gymId(req.getGymId())
                .gymName(req.getGymName())
                .gymAddress(req.getGymAddress())
                .gymPosX(req.getGymPosX())
                .gymPosY(req.getGymPosY())
                .build();

        Gym gym = this.saveGym(gymVo, req.getOperatorId());
        return gymEntityDtoConverter.toVo(gym);
    }
    public GymProcessVo registerGymProcess(GymRegisterProcessRequestDto req) {
        // Gym
        GymVo gymVo = GymVo.builder()
                .id(req.getId())
                .build();

        // GymProcess
        GymProcessVo gymProcessVo = GymProcessVo.builder()
                .id(req.getId())
                .gym(gymVo)
                .gymLastGatheringDate(req.getGymLastGatheringDate())
                .gymGatheringStatusCode(req.getGymGatheringStatusCode())
                .nodeLastCreationDate(req.getNodeLastCreationDate())
                .nodeCreationStatusCode(req.getNodeCreationStatusCode())
                .build();

        GymProcess gymProcess = this.saveGymProcess(gymProcessVo, req.getOperatorId());
        return gymProcessEntityDtoConverter.toVo(gymProcess);
    }
}
