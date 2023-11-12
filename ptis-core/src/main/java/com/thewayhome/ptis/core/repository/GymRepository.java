package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GymRepository extends JpaRepository<Gym, String> {
    Optional<Gym> findByGymId(String gymId);
}