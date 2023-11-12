package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.vo.LinkVo;
import com.thewayhome.ptis.core.dto.request.LinkRegisterRequestDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.LinkRepository;
import com.thewayhome.ptis.core.util.LinkEntityVoConverter;
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
    
    public Optional<Link> findById(String id) {
        return linkRepository.findById(id);
    }
    private Link saveLink(LinkVo req) {
        Link entity = linkEntityDtoConverter.toEntity(req, req.getOperatorId());
        return linkRepository.save(entity);
    }

    @Transactional
    public Link registerLink(LinkRegisterRequestDto req) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("LINK")
                .orElse(new IdSequence("LINK", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        // Link
        LinkVo linkVo = LinkVo.builder()
                .id(req.getId())
                .linkName(req.getLinkName())
                .linkType(req.getLinkType())
                .stNode(req.getStNode())
                .edNode(req.getEdNode())
                .cost(req.getCost())
                .operatorId(req.getOperatorId())
                .build();

        Link link = this.saveLink(linkVo);

        return link;
    }
}
