package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.GymRegisterReqDto;
import com.thewayhome.ptis.core.entity.Gym;
import com.thewayhome.ptis.core.entity.GymProcess;
import com.thewayhome.ptis.core.repository.GymProcessRepository;
import com.thewayhome.ptis.core.repository.GymRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.RestaurantProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GymService {
    @Autowired
    private GymRepository gymRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;
    @Autowired
    private GymProcessRepository gymProcessRepository;

    public Gym saveGym(GymRegisterReqDto req) {
        Gym gym = gymRepository.findByGymId(req.getGymId()).orElse(new Gym());


        // ID
        if (gym.getId() == null) {
            IdSequence idSequence = idSequenceRepository.findById("GYM")
                    .orElse(new IdSequence("GYM", 0L));
            Long nextId = idSequence.getNextId();

            idSequence.setNextId(nextId + 1);
            idSequenceRepository.save(idSequence);

            gym.setId(String.format("%012d", nextId + 1));

            gym.setCreatedAt(LocalDateTime.now());
            gym.setCreatedBy(req.getOperatorId());
        }

        // DATA
        gym.setGymId(req.getGymId());
        gym.setGymName(req.getGymName());
        gym.setGymAddress(req.getGymAddress());
        gym.setGymPosX(req.getGymPosX());
        gym.setGymPosY(req.getGymPosY());


        // DB
        gym.setUpdatedAt(LocalDateTime.now());
        gym.setUpdatedBy(req.getOperatorId());


        // processDB
        GymProcess gymProcess = gymProcessRepository.findById(gym.getId())
                .orElse(new GymProcess());

        if (gymProcess.getId() == null) {
            gymProcess.setId(gym.getId());
            gymProcess.setCreatedAt(LocalDateTime.now());
            gymProcess.setCreatedBy(req.getOperatorId());
            gymProcess.setFirstGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            gymProcess.setGatheringStatusCode("01");
        }
        if (gym.getGymPosX() == null || gym.getGymPosY() == null) {
            gymProcess.setGatheringStatusCode("02");
        }

        gymProcess.setUpdatedAt(LocalDateTime.now());
        gymProcess.setUpdatedBy(req.getOperatorId());
        gymProcess.setLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // ID
        gymProcessRepository.save(gymProcess);

        return gymRepository.save(gym);
    }
}
