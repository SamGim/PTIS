package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.entity.ShortestPathLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortestPathLinkRepository extends JpaRepository<ShortestPathLink, String> {
    Optional<ShortestPathLink> findByStNodeAndEdNode(Node stNode, Node edNode);
}