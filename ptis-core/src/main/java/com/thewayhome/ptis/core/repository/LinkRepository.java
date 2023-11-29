package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.vo.NodeVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
    Optional<Link> findByStNodeAndEdNodeAndLinkType(Node stNode, Node edNode, String linkType);

    Optional<Link> findByStNodeAndEdNode(Node stNode, Node edNode);
}
