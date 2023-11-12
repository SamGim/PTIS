package com.thewayhome.ptis.core.service;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class GymService {
    private final GymRepository gymRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final GymProcessRepository gymProcessRepository;
    private final GymEntityVoConverter gymEntityDtoConverter;
    private final GymProcessEntityVoConverter gymProcessEntityDtoConverter;

    public Gym saveGym(GymVo req) {
        Gym entity = gymEntityDtoConverter.toEntity(req, req.getOperatorId());
        return gymRepository.save(entity);
    }

    public GymProcess saveGymProcess(GymProcessVo req) {
        GymProcess entity = gymProcessEntityDtoConverter.toEntity(req, req.getOperatorId());
        return gymProcessRepository.save(entity);
    }

    public Gym registerGym(GymRegisterRequestDto req) {
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
                .operatorId(req.getOperatorId())
                .build();

        Gym gym = this.saveGym(gymVo);

        // GymProcess
        GymProcessVo gymProcessVo = GymProcessVo.builder()
                .id(req.getId())
                .gym(gymVo)
                .gymFirstGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .gymLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .gymGatheringStatusCode("01")
                .nodeFirstCreationDate(" ")
                .nodeLastCreationDate(" ")
                .nodeCreationStatusCode("00")
                .operatorId(req.getOperatorId())
                .build();

        this.saveGymProcess(gymProcessVo);

        return gym;
    }
}
