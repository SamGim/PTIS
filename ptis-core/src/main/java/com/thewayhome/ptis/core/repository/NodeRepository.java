package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node, String> {
}
