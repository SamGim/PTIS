//package com.thewayhome.ptis.core.util;
//
//import com.thewayhome.ptis.core.entity.Node;
//import com.thewayhome.ptis.core.entity.ShortestPathLink;
//import com.thewayhome.ptis.core.repository.NodeRepository;
//import com.thewayhome.ptis.core.repository.ShortestPathLinkRepository;
//import com.thewayhome.ptis.core.vo.NodeVo;
//import com.thewayhome.ptis.core.vo.ShortestPathLinkVo;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class ShortestPathLinkVoConverter implements IEntityVoConverter<ShortestPathLink, ShortestPathLinkVo>{
//    private final ShortestPathLinkRepository shortestPathLinkRepository;
//    private final NodeRepository nodeRepository;
//    private final NodeEntityVoConverter nodeEntityVoConverter;
//
//    @Override
//    public ShortestPathLink toEntity(ShortestPathLinkVo vo, String operatorId) {
//        log.info("vo: {} 4-1-1", vo);
//        Node stNode = nodeRepository.findById(vo.getStNode().getId()).orElseThrow(IllegalArgumentException::new);
//        Node edNode = nodeRepository.findById(vo.getEdNode().getId()).orElseThrow(IllegalArgumentException::new);
//        Node prevNodeId = nodeRepository.findById(vo.getPrevNode().getId()).orElseThrow(IllegalArgumentException::new);
//        log.info("vo: {} 4-1-2", vo);
//        ShortestPathLink entity = shortestPathLinkRepository.findById(vo.getId())
//                .orElse(
//                        ShortestPathLink.builder()
//                                .id(vo.getId())
//                                .stNodeId(stNode)
//                                .edNodeId(edNode)
//                                .prevNodeId(prevNodeId)
//                                .createdAt(LocalDateTime.now())
//                                .createdBy(operatorId)
//                                .build()
//                );
//
//        if (vo.getPrevNode() != null) entity.setPrevNodeId(prevNodeId);
//        if (vo.getCost() != null) entity.setCost(vo.getCost());
//
//        entity.setUpdatedAt(LocalDateTime.now());
//        entity.setUpdatedBy(operatorId);
//
//        log.info("vo: {} 4-1-3", vo);
//
//        return entity;
//    }
//
//    @Override
//    public ShortestPathLinkVo toVo(ShortestPathLink entity) {
//        NodeVo stNode = null;
//        NodeVo edNode = null;
//        NodeVo prevNode = null;
//
//        if (entity.getStNodeId() != null) stNode = nodeEntityVoConverter.toVo(entity.getStNodeId());
//        if (entity.getEdNodeId() != null) edNode = nodeEntityVoConverter.toVo(entity.getEdNodeId());
//        if (entity.getPrevNodeId() != null) prevNode = nodeEntityVoConverter.toVo(entity.getPrevNodeId());
//
//        return ShortestPathLinkVo.builder()
//                .id(entity.getId())
//                .stNode(stNode)
//                .edNode(edNode)
//                .prevNode(prevNode)
//                .cost(entity.getCost())
//                .build();
//    }
//
//}
