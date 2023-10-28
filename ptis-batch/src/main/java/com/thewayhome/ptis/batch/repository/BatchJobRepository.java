package com.thewayhome.ptis.batch.repository;

import com.thewayhome.ptis.batch.vo.BatchJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobRepository extends JpaRepository<BatchJob, String> {
}
