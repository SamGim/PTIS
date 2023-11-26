package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.BusRouteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRouteCourseRepository extends JpaRepository<BusRouteCourse, String> {
    List<BusRouteCourse> findByBusRouteIds(String busRouteId);
    List<BusRouteCourse> findByBusStationIds(String busStationId);
    List<BusRouteCourse> findByBusRouteIdAndBusStationId(String busRouteId, String busStationId);
}
