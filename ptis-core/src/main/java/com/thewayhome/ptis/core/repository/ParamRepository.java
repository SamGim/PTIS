package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.vo.Param;
import com.thewayhome.ptis.core.vo.ParamKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamRepository extends JpaRepository<Param, ParamKey> {
}
