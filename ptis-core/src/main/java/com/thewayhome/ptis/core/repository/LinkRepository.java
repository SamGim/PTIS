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
    Optional<Link> findByStNodeAndEdNodeAndLinkTypeAndLinkName(Node stNode, Node edNode, String linkType, String linkName);

    @Query("SELECT MIN(l.cost) FROM Link l WHERE l.stNode.id = :stNodeId AND l.edNode.id = :edNodeId")
    Optional<Long> findMinCostLinkByStNodeAndEdNode(String stNodeId, String edNodeId);
    @Query("SELECT l.stNode.id, l.edNode.id, MIN(l.cost) FROM Link l GROUP BY l.stNode.id, l.edNode.id")
    List<Object[]> findAllMinCost();

    // Id가 a보다 크거나 같고 b보다 작거나 같은 모든 링크 쿼리
    @Query("SELECT l FROM Link l WHERE l.id >= :a AND l.id <= :b")
    List<Link> findByIdBetween(String a, String b);

    List<Link> findByStNodeAndEdNode(Node stNode, Node edNode);

    @Query("SELECT l.stNode.id, l.edNode.id, l.cost, l.id FROM Link l " +
            "WHERE (l.stNode.id, l.edNode.id, l.cost) IN " +
            "(SELECT l2.stNode.id, l2.edNode.id, MIN(l2.cost) " +
            "FROM Link l2 " +
            "GROUP BY l2.stNode.id, l2.edNode.id) "
            )
    List<Object[]> findAllMinCostLinks();

    Link findByStNode_IdAndEdNode_IdOrderByCostAsc(String stNodeId, String edNodeId);
}
