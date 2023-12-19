package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.LinkRepository;
import com.thewayhome.ptis.core.util.LinkEntityVoConverter;
import com.thewayhome.ptis.core.util.NodeEntityVoConverter;
import com.thewayhome.ptis.core.vo.LinkVo;
import com.thewayhome.ptis.core.vo.NodeVo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final LinkEntityVoConverter linkEntityDtoConverter;
    private final NodeEntityVoConverter nodeEntityVoConverter;
    
    public Optional<Link> findById(String id) {
        return linkRepository.findById(id);
    }
    private Link saveLink(LinkVo req, String operatorId) {
        Link entity = linkEntityDtoConverter.toEntity(req, operatorId);
        return linkRepository.save(entity);
    }

    @Transactional
    public Link registerLink(LinkRegisterRequestDto req) {
        // ID
        // 이미 ID가 없을 경우 새로 아이디 등록
        if (req.getId() == null || req.getId().isEmpty()) {
            IdSequence idSequence = idSequenceRepository.findById("LINK")
                    .orElse(new IdSequence("LINK", 0L));
            Long id = idSequence.getNextId() + 1;

            idSequence.setNextId(id);
            idSequenceRepository.save(idSequence);

            req.setId(String.format("%012d", id));
        }

        // Link
        LinkVo linkVo = LinkVo.builder()
                .id(req.getId())
                .linkName(req.getLinkName())
                .linkType(req.getLinkType())
                .stNode(req.getStNode())
                .edNode(req.getEdNode())
                .cost(req.getCost())
                .build();

        return this.saveLink(linkVo, req.getOperatorId());
    }



    public Optional<LinkVo> findByStNodeAndEdNodeAndLinkTypeAndLinkName(NodeVo srcNode, NodeVo destNode, String linkType, String linkName, String jobname) {
        Node srcNodeE = nodeEntityVoConverter.toEntity(srcNode, jobname);
        Node destNodeE = nodeEntityVoConverter.toEntity(destNode, jobname);
        return linkRepository.findByStNodeAndEdNodeAndLinkTypeAndLinkName(srcNodeE, destNodeE, linkType, linkName)
                .map(linkEntityDtoConverter::toVo);
    }

    public Optional<Long> findMinCostLinkByStNodeAndEdNode(String stNodeId, String edNodeId) {
        return linkRepository.findMinCostLinkByStNodeAndEdNode(stNodeId, edNodeId);
    }
}
