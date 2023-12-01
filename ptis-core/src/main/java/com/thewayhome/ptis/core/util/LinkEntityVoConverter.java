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
        Node startStation = nodeRepository.findById(vo.getStNode().getId()).orElseThrow(IllegalArgumentException::new);
        Node endStation = nodeRepository.findById(vo.getEdNode().getId()).orElseThrow(IllegalArgumentException::new);

        Link entity = linkRepository.findById(vo.getId())
                .orElse(
                        Link.builder()
                                .id(vo.getId())
                                .stNode(startStation)
                                .edNode(endStation)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getCost() != null) entity.setCost(vo.getCost());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public LinkVo toVo(Link entity) {
        NodeVo startStation = null;
        NodeVo endStation = null;

        if (entity.getStNode() != null) startStation = nodeEntityVoConverter.toVo(entity.getStNode());
        if (entity.getEdNode() != null) endStation = nodeEntityVoConverter.toVo(entity.getEdNode());

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
                .build();
    }
}
