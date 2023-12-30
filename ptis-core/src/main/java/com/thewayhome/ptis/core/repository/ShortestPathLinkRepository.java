package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.ShortestPathLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShortestPathLinkRepository extends JpaRepository<ShortestPathLink, Long> {
    Optional<ShortestPathLink> findByStNodeIdAndEdNodeId(String stNodeId, String edNodeId);

    List<ShortestPathLink> findByStNodeIdAndEdNodeTypeOrderByCostAsc(String companyId, String cx);

    // nativeQuery = true: JPA가 아닌 SQL을 사용할 때
//    @Query(value = "SELECT * FROM shortest_path_link WHERE st_node_id = :stNodeId AND ed_node_id = :edNodeId", nativeQuery = true)
//    Optional<ShortestPathLink> findByStNodeAndEdNode(@Param("stNodeId") String stNodeId, @Param("edNodeId") String edNodeId);
//    Optional<ShortestPathLink> findByStNode_IdAndEdNode_Id(String stNodeId, String edNodeId);
}