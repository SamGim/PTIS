package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.vo.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository  extends JpaRepository<Route, String> {
}
