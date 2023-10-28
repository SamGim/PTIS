package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.vo.BusStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusStationRepository extends JpaRepository<BusStation, String> {
    Optional<BusStation> findByBusStationId(String busStationId);
}
