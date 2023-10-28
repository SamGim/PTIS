package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.vo.IdSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdSequenceRepository  extends JpaRepository<IdSequence, String> {
}
