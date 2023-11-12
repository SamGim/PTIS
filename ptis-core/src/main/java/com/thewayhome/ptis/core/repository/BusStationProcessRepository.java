package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.BusStationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusStationProcessRepository extends JpaRepository<BusStationProcess, String> {
    List<BusStationProcess> findByBusRouteGatheringStatusCodeOrderById(String busRouteGatheringStatusCode);
    List<BusStationProcess> findByBusRouteGatheringStatusCodeNotOrderById(String busRouteGatheringStatusCode);
    List<BusStationProcess> findByBusStationGatheringStatusCodeOrderById(String busStationGatheringStatusCode);
    List<BusStationProcess> findByBusStationGatheringStatusCodeNotOrderById(String busStationGatheringStatusCode);
    List<BusStationProcess> findByNodeCreationStatusCodeOrderById(String nodeCreationStatusCode);
    List<BusStationProcess> findByNodeCreationStatusCodeNotOrderById(String nodeCreationStatusCode);
}
