package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, String> {
    Optional<BusRoute> findByBusRouteId(String busRouteId);
}
