package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.LinkRegisterReqDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

    public Link saveLink(LinkRegisterReqDto req) {
        Link link = new Link();

        // ID
        if (link.getId() == null) {
            IdSequence idSequence = idSequenceRepository.findById("LINK")
                    .orElse(new IdSequence("LINK", 0L));
            Long nextId = idSequence.getNextId();

            idSequence.setNextId(nextId + 1);
            idSequenceRepository.save(idSequence);

            link.setId(String.format("%012d", nextId + 1));

            link.setCreatedAt(LocalDateTime.now());
            link.setCreatedBy(req.getOperatorId());
        }

        // DATA
        link.setLinkName(req.getLinkName());
        link.setLinkType(req.getLinkType());
        link.setStNode(req.getStNode());
        link.setEdNode(req.getEdNode());
        link.setCost(req.getCost());

        // DB
        link.setUpdatedAt(LocalDateTime.now());
        link.setUpdatedBy(req.getOperatorId());

        return linkRepository.save(link);
    }
}
