package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, String> {
    @Query("SELECT o FROM Node o WHERE o.id >= :nodeIdSt AND o.id <= :nodeIdEd")
    List<Node> findByIdsBetween(@Param("nodeIdSt") String nodeIdSt, @Param("nodeIdEd") String nodeIdEd);

}
