package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.BusRouteProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRouteProcessRepository extends JpaRepository<BusRouteProcess, String> {
    List<BusRouteProcess> findByBusRouteGatheringStatusCodeOrderById(String busRouteGatheringStatusCode);
    List<BusRouteProcess> findByBusRouteGatheringStatusCodeNotOrderById(String busRouteGatheringStatusCode);
    List<BusRouteProcess> findByBusStationGatheringStatusCodeOrderById(String busStationGatheringStatusCode);
    List<BusRouteProcess> findByBusStationGatheringStatusCodeNotOrderById(String busStationGatheringStatusCode);
}
