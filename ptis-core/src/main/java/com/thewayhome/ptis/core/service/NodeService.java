package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.NodeRegisterRequestDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.NodeRepository;
import com.thewayhome.ptis.core.util.NodeEntityVoConverter;
import com.thewayhome.ptis.core.vo.BusStationProcessVo;
import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NodeService {
    private final BusStationService busStationService;
    private final NodeRepository nodeRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final NodeEntityVoConverter nodeEntityVoConverter;

    public Optional<Node> findById(String id) {
        return nodeRepository.findById(id);
    }
    public List<NodeVo[]> findByIdsBetween(String srcNodeIdSt, String srcNodeIdEd, String destNodeIdSt, String destNodeIdEd, String operatorId) {
        List<Node> srcNodes = nodeRepository.findByIdsBetween(srcNodeIdSt, srcNodeIdEd);
        List<Node> destNodes = nodeRepository.findByIdsBetween(destNodeIdSt, destNodeIdEd);

        return srcNodes.stream()
                .flatMap(srcNode -> destNodes.stream()
                        .map(destNode -> new NodeVo[]{
                                nodeEntityVoConverter.toVo(srcNode),
                                nodeEntityVoConverter.toVo(destNode),
                        })
                )
                .toList();
    }

    public Node saveNode(NodeVo req, String operatorId) {
        Node entity = nodeEntityVoConverter.toEntity(req, operatorId);
        return nodeRepository.save(entity);
    }
    public Node createNodeFromBusStation(NodeRegisterRequestDto req, String busStationId) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("NODE")
                .orElse(new IdSequence("NODE", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        // Node
        NodeVo nodeVo = NodeVo.builder()
                .id(req.getId())
                .nodeName(req.getNodeName())
                .nodeSrcType("bs")
                .nodeSrcId(busStationId)
                .nodePosX(req.getNodePosX())
                .nodePosY(req.getNodePosY())
                .build();

        Node node = this.saveNode(nodeVo, req.getOperatorId());

        // BusStationProcess
        BusStationProcessVo busStationProcessVo = BusStationProcessVo.builder()
                .id(busStationId)
                .nodeCreationStatusCode("01")
                .nodeLastCreationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        busStationService.saveBusStationProcess(busStationProcessVo, req.getOperatorId());

        return node;
    }

    public Node createNodeFromCompany(NodeRegisterRequestDto nodeRegisterReqDto, String companyID) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("NODE")
                .orElse(new IdSequence("NODE", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        nodeRegisterReqDto.setId(String.format("%012d", id));

        // Node
        NodeVo nodeVo = NodeVo.builder()
                .id(nodeRegisterReqDto.getId())
                .nodeName(nodeRegisterReqDto.getNodeName())
                .nodeSrcType("cp")
                .nodeSrcId(companyID)
                .nodePosX(nodeRegisterReqDto.getNodePosX())
                .nodePosY(nodeRegisterReqDto.getNodePosY())
                .build();

        Node node = this.saveNode(nodeVo, nodeRegisterReqDto.getOperatorId());

        return node;
    }

    public Node createNodeFromComplex(NodeRegisterRequestDto nodeRegisterReqDto, String complexId) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("NODE")
                .orElse(new IdSequence("NODE", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        nodeRegisterReqDto.setId(String.format("%012d", id));

        // Node
        NodeVo nodeVo = NodeVo.builder()
                .id(nodeRegisterReqDto.getId())
                .nodeName(nodeRegisterReqDto.getNodeName())
                .nodeSrcType("cx")
                .nodeSrcId(complexId)
                .nodePosX(nodeRegisterReqDto.getNodePosX())
                .nodePosY(nodeRegisterReqDto.getNodePosY())
                .build();

        Node node = this.saveNode(nodeVo, nodeRegisterReqDto.getOperatorId());

        return node;
    }

    public List<NodeVo> findAll(String jobName) {
        return nodeRepository.findAll().stream()
                .map(node -> nodeEntityVoConverter.toVo(node))
                .toList();
    }

    public boolean isExist(String id) {
        return nodeRepository.existsByNodeName(id);
    }

    public NodeVo findByBusStationId(String busStationId, String jobName){
        Node node = nodeRepository.findByNodeSrcId(busStationId).orElseThrow(() -> new IllegalArgumentException("해당 버정이 존재하지 않습니다. id=" + busStationId));
        return nodeEntityVoConverter.toVo(node);
    }
}
