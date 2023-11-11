package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.NodeRegisterReqDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.repository.BusStationProcessRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NodeService {
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private BusStationProcessRepository busStationProcessRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;

    public Node createNodeFromBusStation(NodeRegisterReqDto req, String busStationId) {
        Node node = new Node();

        // ID
        if (node.getId() == null) {
            IdSequence idSequence = idSequenceRepository.findById("NODE")
                    .orElse(new IdSequence("NODE", 0L));
            Long nextId = idSequence.getNextId();

            idSequence.setNextId(nextId + 1);
            idSequenceRepository.save(idSequence);

            node.setId(String.format("%012d", nextId + 1));

            node.setCreatedAt(LocalDateTime.now());
            node.setCreatedBy(req.getOperatorId());
        }

        // DATA
        node.setNodeName(req.getNodeName());
        node.setNodePosX(req.getNodePosX());
        node.setNodePosY(req.getNodePosY());

        // DB
        node.setUpdatedAt(LocalDateTime.now());
        node.setUpdatedBy(req.getOperatorId());

        busStationProcessRepository.findById(busStationId)
                .ifPresent(busStationProcess -> {
                    busStationProcess.setNodeCreationStatusCode("01");
                    busStationProcess.getNodes().add(node);
                    busStationProcessRepository.save(busStationProcess);
                });

        return nodeRepository.save(node);
    }

    public Node saveNode(NodeRegisterReqDto req) {
        Node node = new Node();

        // ID
        if (node.getId() == null) {
            IdSequence idSequence = idSequenceRepository.findById("NODE")
                    .orElse(new IdSequence("NODE", 0L));
            Long nextId = idSequence.getNextId();

            idSequence.setNextId(nextId + 1);
            idSequenceRepository.save(idSequence);

            node.setId(String.format("%012d", nextId + 1));

            node.setCreatedAt(LocalDateTime.now());
            node.setCreatedBy(req.getOperatorId());
        }

        // DATA
        node.setNodeName(req.getNodeName());
        node.setNodePosX(req.getNodePosX());
        node.setNodePosY(req.getNodePosY());

        // DB
        node.setUpdatedAt(LocalDateTime.now());
        node.setUpdatedBy(req.getOperatorId());

        return nodeRepository.save(node);
    }
}
