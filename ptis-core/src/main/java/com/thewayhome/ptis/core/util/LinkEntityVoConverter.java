package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.LinkVo;
import com.thewayhome.ptis.core.vo.NodeVo;
import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.repository.LinkRepository;
import com.thewayhome.ptis.core.repository.NodeRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LinkEntityVoConverter implements IEntityVoConverter<Link, LinkVo> {
    private final LinkRepository linkRepository;
    private final NodeRepository nodeRepository;
    private final NodeEntityVoConverter nodeEntityVoConverter;

    @Override
    @NotNull
    public Link toEntity(LinkVo vo, String operatorId) {
        Node startStation = nodeRepository.findById(vo.getStNode().getId()).orElse(null);
        Node endStation = nodeRepository.findById(vo.getEdNode().getId()).orElse(null);

        Link entity = linkRepository.findById(vo.getId())
                .orElse(
                        Link.builder()
                                .id(vo.getId())
                                .linkName(vo.getLinkName())
                                .linkType(vo.getLinkType())
                                .stNode(startStation)
                                .edNode(endStation)
                                .cost(vo.getCost())
                                .createdAt(LocalDateTime.now())
                                .createdBy(vo.getOperatorId())
                                .build()
                );


        entity.setLinkName(vo.getLinkName() == null ? entity.getLinkName() : vo.getLinkName());
        entity.setLinkType(vo.getLinkType() == null ? entity.getLinkType() : vo.getLinkType());
        entity.setStNode(startStation == null ? entity.getStNode() : startStation);
        entity.setEdNode(endStation == null ? entity.getEdNode() : endStation);
        entity.setCost(vo.getCost() == null ? entity.getCost() : vo.getCost());

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? vo.getOperatorId() : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? vo.getOperatorId() : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public LinkVo toVo(Link entity, String operatorId) {
        NodeVo startStation = nodeEntityVoConverter.toVo(entity.getStNode(), operatorId);
        NodeVo endStation = nodeEntityVoConverter.toVo(entity.getEdNode(), operatorId);
        return LinkVo.builder()
                .id(entity.getId())
                .linkName(entity.getLinkName())
                .linkType(entity.getLinkType())
                .stNode(startStation)
                .edNode(endStation)
                .cost(entity.getCost())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
