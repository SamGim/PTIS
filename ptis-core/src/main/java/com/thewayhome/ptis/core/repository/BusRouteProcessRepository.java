package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.vo.BusRouteProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRouteProcessRepository extends JpaRepository<BusRouteProcess, String> {
    List<BusRouteProcess> findByGatheringStatusCodeOrderById(String gatheringStatusCode);
    List<BusRouteProcess> findByGatheringStatusCodeNotOrderById(String gatheringStatusCode);
    @Query("SELECT o FROM BusRouteProcess o WHERE o.firstGatheringDate >= :startDate AND o.firstGatheringDate <= :endDate")
    List<BusRouteProcess> findByFirstGatheringDateInDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
    @Query("SELECT o FROM BusRouteProcess o WHERE o.lastGatheringDate >= :startDate AND o.lastGatheringDate <= :endDate")
    List<BusRouteProcess> findByLastGatheringDateInDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
