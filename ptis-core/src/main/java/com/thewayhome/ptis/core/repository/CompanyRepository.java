package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}