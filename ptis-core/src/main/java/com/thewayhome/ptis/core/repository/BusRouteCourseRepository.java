package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.BusRouteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BusRouteCourseRepository extends JpaRepository<BusRouteCourse, String> {
    List<BusRouteCourse> findByBusRouteId(String busRouteId);
    List<BusRouteCourse> findByBusStationIdAndFirstBusTimeIsNotNull(String busStationId);
    List<BusRouteCourse> findByBusRouteIdAndBusStationId(String busRouteId, String busStationId);
    List<BusRouteCourse> findByFirstBusTimeIsNotNullAndBusRouteIdAndFirstBusTimeAfter(String busRouteId, LocalTime firstBusTime);
}
