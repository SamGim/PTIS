package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.BusStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusStationRepository extends JpaRepository<BusStation, String> {
    @Query("SELECT b FROM BusStation b WHERE b.busStationId = :busStationId")
    Optional<BusStation> findByBusStationId(String busStationId);
}
