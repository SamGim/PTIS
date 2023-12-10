package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
    Optional<Link> findByStNodeAndEdNodeAndLinkType(Node stNode, Node edNode, String linkType);

    @Query("SELECT MIN(l.cost) FROM Link l WHERE l.stNode.id = :stNodeId AND l.edNode.id = :edNodeId")
    Optional<Long> findMinCostLinkByStNodeAndEdNode(String stNodeId, String edNodeId);
    @Query("SELECT l.stNode.id, l.edNode.id, MIN(l.cost) FROM Link l GROUP BY l.stNode.id, l.edNode.id")
    List<Object[]> findAllMinCost();
    List<Link> findByStNodeAndEdNode(Node stNode, Node edNode);
}
