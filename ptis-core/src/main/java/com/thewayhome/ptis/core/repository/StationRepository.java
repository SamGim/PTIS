package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
}
