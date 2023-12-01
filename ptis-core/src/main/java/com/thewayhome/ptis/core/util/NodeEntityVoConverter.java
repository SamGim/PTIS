package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.NodeVo;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.repository.NodeRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NodeEntityVoConverter implements IEntityVoConverter<Node, NodeVo> {
    private final NodeRepository nodeRepository;

    @Override
    @NotNull
    public Node toEntity(NodeVo vo, String operatorId) {
        Node entity = nodeRepository.findById(vo.getId())
                .orElse(
                        Node.builder()
                                .id(vo.getId())
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getNodeName() != null) entity.setNodeName(vo.getNodeName());
        if (vo.getNodePosX() != null) entity.setNodePosX(vo.getNodePosX());
        if (vo.getNodePosY() != null) entity.setNodePosY(vo.getNodePosY());
        if (vo.getNodeSrcType() != null) entity.setNodeSrcType(vo.getNodeSrcType());
        if (vo.getNodeSrcId() != null) entity.setNodeSrcId(vo.getNodeSrcId());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public NodeVo toVo(Node entity) {
        return NodeVo.builder()
                .id(entity.getId())
                .nodeName(entity.getNodeName())
                .nodeSrcType(entity.getNodeSrcType())
                .nodeSrcId(entity.getNodeSrcId())
                .nodePosX(entity.getNodePosX())
                .nodePosY(entity.getNodePosY())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
