package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.entity.ShortestPathLink;
import com.thewayhome.ptis.core.repository.NodeRepository;
import com.thewayhome.ptis.core.repository.ShortestPathLinkRepository;
import com.thewayhome.ptis.core.vo.NodeVo;
import com.thewayhome.ptis.core.vo.ShortestPathLinkVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ShortestPathLinkVoConverter implements IEntityVoConverter<ShortestPathLink, ShortestPathLinkVo>{
    private final ShortestPathLinkRepository shortestPathLinkRepository;
    private final NodeRepository nodeRepository;
    private final NodeEntityVoConverter nodeEntityVoConverter;

    @Override
    public ShortestPathLink toEntity(ShortestPathLinkVo vo, String operatorId) {
        Node stNode = nodeRepository.findById(vo.getStNode().getId()).orElseThrow(IllegalArgumentException::new);
        Node edNode = nodeRepository.findById(vo.getEdNode().getId()).orElseThrow(IllegalArgumentException::new);
        Node prevNodeId = nodeRepository.findById(vo.getPrevNode().getId()).orElseThrow(IllegalArgumentException::new);
        ShortestPathLink entity = shortestPathLinkRepository.findById(vo.getId())
                .orElse(
                        ShortestPathLink.builder()
                                .id(vo.getId())
                                .stNode(stNode)
                                .edNode(edNode)
                                .prevNode(prevNodeId)
                                .createdAt(LocalDateTime.now())
                                .createdBy(vo.getCreatedBy())
                                .build()
                );


        if (vo.getCost() != null) entity.setCost(vo.getCost());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    public ShortestPathLinkVo toVo(ShortestPathLink entity) {
        NodeVo stNode = null;
        NodeVo edNode = null;
        NodeVo prevNode = null;

        if (entity.getStNode() != null) stNode = nodeEntityVoConverter.toVo(entity.getStNode());
        if (entity.getEdNode() != null) edNode = nodeEntityVoConverter.toVo(entity.getEdNode());
        if (entity.getPrevNode() != null) prevNode = nodeEntityVoConverter.toVo(entity.getPrevNode());

        return ShortestPathLinkVo.builder()
                .id(entity.getId())
                .stNode(stNode)
                .edNode(edNode)
                .prevNode(prevNode)
                .cost(entity.getCost())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

}
