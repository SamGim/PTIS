package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.SPLResponseDto;
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
//import com.thewayhome.ptis.core.util.ShortestPathLinkVoConverter;
import com.thewayhome.ptis.core.vo.NodeVo;
import com.thewayhome.ptis.core.vo.ShortestPathLinkVo;
import jakarta.transaction.Transactional;
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
//    private final ShortestPathLinkVoConverter shortestPathLinkVoConverter;
    private final LinkRepository linkRepository;
    private final IdSequenceRepository idSequenceRepository;
//
//    public ShortestPathLinkVo getSPLBySrcNodeIdAndDestNodeId(NodeVo stNodeVo, NodeVo edNodeVo, String jobName) {
//        Node stNode = nodeEntityVoConverter.toEntity(stNodeVo, jobName);
//        Node edNode = nodeEntityVoConverter.toEntity(edNodeVo, jobName);
////        log.info("stNode: {}, edNode: {}", stNode, edNode);
//        Optional<ShortestPathLink> resSpl = shortestPathLinkRepository.findByStNode_IdAndEdNode_Id(stNodeVo.getId(), edNodeVo.getId());
////        log.info("stNode: {}, edNode: {}-----", stNode, edNode);
//        if (resSpl.isPresent()) {
//            return shortestPathLinkVoConverter.toVo(resSpl.get());
//        } else {
//            List<Link> links = linkRepository.findByStNodeAndEdNode(stNode, edNode);
//            if (links.size() == 0) {
//                throw new IllegalArgumentException("No link found");
//            } else {
//                ShortestPathLinkVo shortestPathLinkVo = ShortestPathLinkVo.builder()
//                        .stNode(stNodeVo)
//                        .edNode(edNodeVo)
//                        .prevNode(stNodeVo)
//                        .cost(Long.MAX_VALUE)
//                        .build();
//
//                Optional<Link> minLink = links.stream().min(Comparator.comparing(Link::getCost));
//                minLink.ifPresent(link -> shortestPathLinkVo.setCost(link.getCost()));
//
//                this.registerShortestPathLink(
//                        ShortestPathLinkRegisterDto.builder()
//                                .id(null)
//                                .stNode(shortestPathLinkVo.getStNode())
//                                .edNode(shortestPathLinkVo.getEdNode())
//                                .prevNode(shortestPathLinkVo.getPrevNode())
//                                .cost(shortestPathLinkVo.getCost())
//                                .operatorId(jobName)
//                                .build()
//                );
//                return shortestPathLinkVo;
//            }
//        }
//    }
//
//    // id가 이미 있는 SPL이면 업데이트, 없으면 새로 등록
//    @Transactional
//    public ShortestPathLink registerShortestPathLink(ShortestPathLinkRegisterDto req) {
//        log.info("req: {} 1", req);
//        if (req.getId() != null) {
//            Optional<ShortestPathLink> resSpl = shortestPathLinkRepository.findById(req.getId());
//            log.info("req: {} 1-1", req);
//            if (resSpl.isPresent()) {
//                ShortestPathLinkVo shortestPathLinkVo = shortestPathLinkVoConverter.toVo(resSpl.get());
//                shortestPathLinkVo.setCost(req.getCost());
//                shortestPathLinkVo.setEdNode(req.getEdNode());
//                shortestPathLinkVo.setPrevNode(req.getPrevNode());
//                shortestPathLinkVo.setStNode(req.getStNode());
//                return this.saveShortestPathLink(shortestPathLinkVo, req.getOperatorId());
//            }
//            log.info("req: {} 1-2", req);
//        }
//        log.info("req: {} 2", req);
//        // req에 id가 없거나, id가 있어도 SPL에 없으면 새로 등록
//        IdSequence idSequence = idSequenceRepository.findById("SPL")
//                .orElse(new IdSequence("SPL", 0L));
//        Long id = idSequence.getNextId() + 1;
//
//        idSequence.setNextId(id);
//        idSequenceRepository.save(idSequence);
//
//        log.info("req: {} 3", req);
//
//        req.setId(String.format("%012d", id));
//
//        // ShortestPathLink
//        ShortestPathLinkVo shortestPathLinkVo = ShortestPathLinkVo.builder()
//                .id(req.getId())
//                .stNode(req.getStNode())
//                .edNode(req.getEdNode())
//                .prevNode(req.getPrevNode())
//                .cost(req.getCost())
//                .build();
//        log.info("req: {} 4", req);
//
//        return this.saveShortestPathLink(shortestPathLinkVo, req.getOperatorId());
//    }
//
//    private ShortestPathLink saveShortestPathLink(ShortestPathLinkVo req, String operatorId) {
//        log.info("req: {} 4-1", req);
//        ShortestPathLink entity = shortestPathLinkVoConverter.toEntity(req, operatorId);
//        log.info("req: {} 4-2", req);
//        return shortestPathLinkRepository.save(entity);
//    }
//
//    private ShortestPathLink saveShortestPathLinkD(ShortestPathLink req, String operatorId) {
//        return shortestPathLinkRepository.save(req);
//    }

    public List<Object> getSplListByStNodeAndEdNode(String stNodeId, String edNodeId) {
        // 반환할 빈 res 리스트 생성
        List<Object> res = new ArrayList<>();
        ShortestPathLink curSpl = shortestPathLinkRepository.findByStNodeIdAndEdNodeId(stNodeId, edNodeId).orElseThrow(() -> new IllegalArgumentException("No SPL found"));
        String prevNodeId = curSpl.getPrevNodeId();
        if (prevNodeId.equals(curSpl.getStNodeId())) {
            res.add(Arrays.asList(curSpl.getStNodeId(), curSpl.getEdNodeId(), curSpl.getCost(), curSpl.getLinkType()));
            return res;
        }
        else {
            // prevNodeid가 stNodeId가 아니라면
            // stNode부터 prevNode까지의 SPL을 res에 추가하고, prevNode를 stNode로 바꾸고 stNode부터 edNode까지의 SPL을 다시 찾는다.
            while (!prevNodeId.equals(curSpl.getStNodeId())) {
                curSpl = shortestPathLinkRepository.findByStNodeIdAndEdNodeId(curSpl.getStNodeId(), curSpl.getPrevNodeId()).orElseThrow(() -> new IllegalArgumentException("No SPL found"));
                res.add(Arrays.asList(curSpl.getStNodeId(), curSpl.getEdNodeId(), curSpl.getCost(), curSpl.getLinkType()));
                stNodeId = curSpl.getPrevNodeId();
            }
        }
        return res;
    }
    public ArrayList<SPLResponseDto> getSplByStNodeAndEdNodeByRecursive(String stNodeId, String edNodeId){
        ArrayList<SPLResponseDto> res = new ArrayList<>();
        ShortestPathLink curSpl = shortestPathLinkRepository.findByStNodeIdAndEdNodeId(stNodeId, edNodeId).orElseThrow(() -> new IllegalArgumentException("No SPL found " + stNodeId + " -> " + edNodeId));
        String prevNodeId = curSpl.getPrevNodeId();
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
                            .linkType(curSpl.getLinkType())
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
                            .linkType(curSpl.getLinkType())
                            .build()
            );
            res.addAll(getSplByStNodeAndEdNodeByRecursive(curSpl.getEdNodeId(), edNodeId));
        }
        return res;
    }
}
