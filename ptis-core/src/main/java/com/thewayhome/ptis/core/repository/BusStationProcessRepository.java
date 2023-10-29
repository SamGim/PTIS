package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.vo.BusStationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusStationProcessRepository extends JpaRepository<BusStationProcess, String> {
    List<BusStationProcess> findByGatheringStatusCode(String gatheringStatusCode);
    List<BusStationProcess> findByGatheringStatusCodeNot(String gatheringStatusCode);
    @Query("SELECT o FROM BusStationProcess o WHERE o.firstGatheringDate >= :startDate AND o.firstGatheringDate <= :endDate")
    List<BusStationProcess> findByFirstGatheringDateInDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
    @Query("SELECT o FROM BusStationProcess o WHERE o.lastGatheringDate >= :startDate AND o.lastGatheringDate <= :endDate")
    List<BusStationProcess> findByLastGatheringDateInDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
