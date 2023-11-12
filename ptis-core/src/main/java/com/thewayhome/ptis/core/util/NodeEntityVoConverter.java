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
                                .nodeName(vo.getNodeName())
                                .nodePosX(vo.getNodePosX())
                                .nodePosY(vo.getNodePosY())
                                .createdAt(LocalDateTime.now())
                                .createdBy(vo.getOperatorId())
                                .build()
                );


        entity.setNodeName(vo.getNodeName() == null ? entity.getNodeName() : vo.getNodeName());
        entity.setNodePosX(vo.getNodePosX() == null ? entity.getNodePosX() : vo.getNodePosX());
        entity.setNodePosY(vo.getNodePosY() == null ? entity.getNodePosY() : vo.getNodePosY());

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? vo.getOperatorId() : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? vo.getOperatorId() : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public NodeVo toVo(Node entity, String operatorId) {
        return NodeVo.builder()
                .id(entity.getId())
                .nodeName(entity.getNodeName())
                .nodePosX(entity.getNodePosX())
                .nodePosY(entity.getNodePosY())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
