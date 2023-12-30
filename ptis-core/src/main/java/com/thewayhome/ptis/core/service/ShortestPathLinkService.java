package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.SPLResponseDto;
import com.thewayhome.ptis.core.dto.request.ShortestPathLinkRegisterDto;
import com.thewayhome.ptis.core.dto.response.ComplexTimeDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Link;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.entity.ShortestPathLink;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.LinkRepository;
import com.thewayhome.ptis.core.repository.NodeRepository;
import com.thewayhome.ptis.core.repository.ShortestPathLinkRepository;
import com.thewayhome.ptis.core.util.NodeEntityVoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortestPathLinkService {
    private final ShortestPathLinkRepository shortestPathLinkRepository;
    private final NodeRepository nodeRepository;
    private final NodeEntityVoConverter nodeEntityVoConverter;
    private final LinkRepository linkRepository;
    private final IdSequenceRepository idSequenceRepository;

    public ArrayList<SPLResponseDto> getSplByStNodeAndEdNodeByRecursive(String stNodeId, String edNodeId){
        ArrayList<SPLResponseDto> res = new ArrayList<>();
        ShortestPathLink curSpl = shortestPathLinkRepository.findByStNodeIdAndEdNodeId(stNodeId, edNodeId).orElseThrow(() -> new IllegalArgumentException("No SPL found " + stNodeId + " -> " + edNodeId));
        String prevNodeId = curSpl.getPrevNodeId();
        String linkId = curSpl.getLinkId();
        Link link = linkRepository.findById(linkId).orElseThrow(() -> new IllegalArgumentException("No Link found " + linkId));
        String linkName = link.getLinkName();
        String linkType = link.getLinkType();
        if (prevNodeId.equals(curSpl.getStNodeId())) {
            Node stNode = nodeRepository.findById(stNodeId).orElseThrow(() -> new IllegalArgumentException("No Node found " + stNodeId));
            Node edNode = nodeRepository.findById(edNodeId).orElseThrow(() -> new IllegalArgumentException("No Node found " + edNodeId));
            res.add(
                    SPLResponseDto.builder()
                            .stNodeName(stNode.getNodeName())
                            .stNodeSrcType(stNode.getNodeSrcType())
                            .edNodeName(edNode.getNodeName())
                            .edNodeSrcType(edNode.getNodeSrcType())
                            .cost(curSpl.getCost().toString())
                            .linkName(linkName)
                            .linkType(linkType)
                            .build()
            );
        }
        else {
            curSpl = shortestPathLinkRepository.findByStNodeIdAndEdNodeId(stNodeId, prevNodeId).orElseThrow(() -> new IllegalArgumentException("No SPL found " + stNodeId + " -> " + prevNodeId));
            Node stNode = nodeRepository.findById(stNodeId).orElseThrow(() -> new IllegalArgumentException("No Node found " + stNodeId));
            Node edNode = nodeRepository.findById(prevNodeId).orElseThrow(() -> new IllegalArgumentException("No Node found " + prevNodeId));
            res.add(
                    SPLResponseDto.builder()
                            .stNodeName(stNode.getNodeName())
                            .stNodeSrcType(stNode.getNodeSrcType())
                            .edNodeName(edNode.getNodeName())
                            .edNodeSrcType(edNode.getNodeSrcType())
                            .cost(curSpl.getCost().toString())
                            .linkName(linkName)
                            .linkType(linkType)
                            .build()
            );
            res.addAll(getSplByStNodeAndEdNodeByRecursive(curSpl.getEdNodeId(), edNodeId));
        }
        return res;
    }

    public List<ComplexTimeDto> getComplexIdsAndDuration(String companyId) {
        // companyid를 stNode로 가지면서 edNodeSrc가 "cx"인 SPL을 time기준 상위 10개만 가져온다.
        List<ShortestPathLink> splList = shortestPathLinkRepository.findByStNodeIdAndEdNodeTypeOrderByCostAsc(companyId, "bs");
        log.info("companyId: {}", companyId);
        log.info("splList: {}", splList);
        List<ComplexTimeDto> res = new ArrayList<>();
        for(ShortestPathLink spl : splList) {
            res.add(
                    ComplexTimeDto.builder()
                            .id(Long.valueOf(spl.getEdNodeId()))
                            .duration(Math.toIntExact(spl.getCost()))
                            .build()
            );
        }
        return res;
    }
}
