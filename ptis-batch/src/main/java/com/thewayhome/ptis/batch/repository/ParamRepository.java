package com.thewayhome.ptis.batch.repository;

import com.thewayhome.ptis.batch.vo.Param;
import com.thewayhome.ptis.batch.vo.ParamKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamRepository extends JpaRepository<Param, ParamKey> {
}
