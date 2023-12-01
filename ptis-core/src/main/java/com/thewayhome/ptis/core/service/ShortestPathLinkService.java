package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.ShortestPathLinkRegisterDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.entity.ShortestPathLink;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.LinkRepository;
import com.thewayhome.ptis.core.repository.NodeRepository;
import com.thewayhome.ptis.core.repository.ShortestPathLinkRepository;
import com.thewayhome.ptis.core.util.NodeEntityVoConverter;
import com.thewayhome.ptis.core.util.ShortestPathLinkVoConverter;
import com.thewayhome.ptis.core.vo.NodeVo;
import com.thewayhome.ptis.core.vo.ShortestPathLinkVo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortestPathLinkService {
    private final ShortestPathLinkRepository shortestPathLinkRepository;
    private final NodeRepository nodeRepository;
    private final NodeEntityVoConverter nodeEntityVoConverter;
    private final ShortestPathLinkVoConverter shortestPathLinkVoConverter;
    private final LinkRepository linkRepository;
    private final IdSequenceRepository idSequenceRepository;

    public ShortestPathLinkVo getSPLBySrcNodeIdAndDestNodeId(NodeVo stNodeVo, NodeVo edNodeVo, String jobName) {
        Node stNode = nodeEntityVoConverter.toEntity(stNodeVo, jobName);
        Node edNode = nodeEntityVoConverter.toEntity(edNodeVo, jobName);
        Optional<ShortestPathLink> resSpl = shortestPathLinkRepository.findByStNodeAndEdNode(stNode, edNode);
        if (resSpl.isPresent()) {
            return shortestPathLinkVoConverter.toVo(resSpl.get());
        } else {
            List<Link> links = linkRepository.findByStNodeAndEdNode(stNode, edNode);
            if (links.size() == 0) {
                throw new IllegalArgumentException("No link found");
            } else {
                ShortestPathLinkVo shortestPathLinkVo = ShortestPathLinkVo.builder()
                        .stNode(stNodeVo)
                        .edNode(edNodeVo)
                        .prevNode(stNodeVo)
                        .cost(Long.MAX_VALUE)
                        .build();

                Optional<Link> minLink = links.stream().min(Comparator.comparing(Link::getCost));
                minLink.ifPresent(link -> shortestPathLinkVo.setCost(link.getCost()));

                this.registerShortestPathLink(
                        ShortestPathLinkRegisterDto.builder()
                                .id(null)
                                .stNode(shortestPathLinkVo.getStNode())
                                .edNode(shortestPathLinkVo.getEdNode())
                                .prevNode(shortestPathLinkVo.getPrevNode())
                                .cost(shortestPathLinkVo.getCost())
                                .build()
                );
                return shortestPathLinkVo;
            }
        }
    }

    // id가 이미 있는 SPL이면 업데이트, 없으면 새로 등록
    @Transactional
    public ShortestPathLink registerShortestPathLink(ShortestPathLinkRegisterDto req) {
        if (req.getId() != null) {
            Optional<ShortestPathLink> resSpl = shortestPathLinkRepository.findById(req.getId());
            if (resSpl.isPresent()) {
                ShortestPathLinkVo shortestPathLinkVo = shortestPathLinkVoConverter.toVo(resSpl.get());
                shortestPathLinkVo.setCost(req.getCost());
                shortestPathLinkVo.setEdNode(req.getEdNode());
                shortestPathLinkVo.setPrevNode(req.getPrevNode());
                shortestPathLinkVo.setStNode(req.getStNode());
                return this.saveShortestPathLink(shortestPathLinkVo, req.getOperatorId());
            }
        }
        // req에 id가 없거나, id가 있어도 SPL에 없으면 새로 등록
        IdSequence idSequence = idSequenceRepository.findById("SPL")
                .orElse(new IdSequence("SPL", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        // ShortestPathLink
        ShortestPathLinkVo shortestPathLinkVo = ShortestPathLinkVo.builder()
                .id(req.getId())
                .stNode(req.getStNode())
                .edNode(req.getEdNode())
                .prevNode(req.getPrevNode())
                .cost(req.getCost())
                .build();

        return this.saveShortestPathLink(shortestPathLinkVo, req.getOperatorId());
    }

    private ShortestPathLink saveShortestPathLink(ShortestPathLinkVo req, String operatorId) {
        ShortestPathLink entity = shortestPathLinkVoConverter.toEntity(req, operatorId);
        return shortestPathLinkRepository.save(entity);
    }
}
