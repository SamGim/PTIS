package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.SPLResponseDto;
import com.thewayhome.ptis.core.dto.request.ShortestPathLinkRegisterDto;
import com.thewayhome.ptis.core.dto.response.ComplexTimeDto;
import com.thewayhome.ptis.core.entity.*;
import com.thewayhome.ptis.core.entity.complex.RealComplex;
import com.thewayhome.ptis.core.repository.*;
import com.thewayhome.ptis.core.util.NodeEntityVoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortestPathLinkService {
    private final RealComplexRepository realComplexRepository;
    private final ShortestPathLinkRepository shortestPathLinkRepository;
    private final NodeRepository nodeRepository;
    private final NodeEntityVoConverter nodeEntityVoConverter;
    private final LinkRepository linkRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final CompanyRepository companyRepository;


    public ArrayList<SPLResponseDto> getSplFull(Long stNodeId, Long edNodeId){
        RealComplex RealComplex = realComplexRepository.findById(edNodeId).orElseThrow(() -> new IllegalArgumentException("No RealComplex found " + stNodeId));
        Company company = companyRepository.findById(stNodeId).orElseThrow(() -> new IllegalArgumentException("No Company found " + stNodeId));
        String stBusStationId = company.getNearestNodeId();
        Node stNode = nodeRepository.findById(stBusStationId).orElseThrow(() -> new IllegalArgumentException("No Node found " + stBusStationId));
        String edBusStationId = RealComplex.getNearestNodeId();
        Node edNode = nodeRepository.findById(edBusStationId).orElseThrow(() -> new IllegalArgumentException("No Node found " + edBusStationId));
        ArrayList<SPLResponseDto> res = new ArrayList<>();
        res.add(
                SPLResponseDto.builder()
                        .stNodeName(company.getCompanyName())
                        .stNodeSrcType("cp")
                        .edNodeName(stNode.getNodeName())
                        .edNodeSrcType(stNode.getNodeSrcType())
                        .cost(company.getNearestNodeTime().toString())
                        .linkName(String.format("%s-%s", company.getCompanyName(), stNode.getNodeName()))
                        .linkType("W")
                        .build()
        );
        res.addAll(getSplByStNodeAndEdNodeByRecursive(stBusStationId, edBusStationId));
        res.add(
                SPLResponseDto.builder()
                        .stNodeName(edNode.getNodeName())
                        .stNodeSrcType(edNode.getNodeSrcType())
                        .edNodeName(RealComplex.getName())
                        .edNodeSrcType("cx")
                        .cost(RealComplex.getNearestNodeTime().toString())
                        .linkName(String.format("%s-%s", edNode.getNodeName(), RealComplex.getName()))
                        .linkType("W")
                        .build()
        );
        return res;
    }
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

    // 모든 매물을 불러와서 직장과의 시간을 계산해 시간 기준으로 정렬한다.
    // 10개만 complexTimeDto로 변환하여 반환한다.
    // 만약 nearestTime이 없는 매물은? -> 예외처리
    public List<ComplexTimeDto> getComplexIdsAndDuration(String companyId) {
        // 전체 매물 가져오기
        List<RealComplex> realComplexes = realComplexRepository.findAll();
        List<ComplexTimeDto> complexTimeDtos = new ArrayList<>();

        Company company = companyRepository.findById(Long.parseLong(companyId)).orElseThrow(() -> new IllegalArgumentException("No Company found " + companyId));
        if (company.getNearestNodeTime() == null) {
            throw new IllegalArgumentException("No nearestNodeTime found " + companyId);
        }
        String stNodeId = company.getNearestNodeId();
        Long duration1 = company.getNearestNodeTime();
        log.info("직장부터 가장 가까운 정류장까지 소요시간 : {}", duration1);

        for (RealComplex realComplex : realComplexes) {
            // 직장부터 가장 가까운 노드까지의 거리 계산
            // 해당 노드부터 매물과 가장 가까운 노드까지의 거리 계산
            // 매물부터 가장 가까운 노드까지의 거리 계산
            Long complexId = realComplex.getId();
            String edNodeId = realComplex.getNearestNodeId();
            log.info("매물부터 가장 가까운 정류장까지 소요시간 : {}", realComplex.getNearestNodeTime());
            if (realComplex.getNearestNodeTime() == null) {
                throw new IllegalArgumentException("No nearestNodeTime found " + complexId);
            }
            Long duration3 = realComplex.getNearestNodeTime();
            ShortestPathLink spl = shortestPathLinkRepository.findByStNodeIdAndEdNodeId(stNodeId, edNodeId).orElseThrow(() -> new IllegalArgumentException("No SPL found " + stNodeId + " -> " + edNodeId));
            Long duration2 = spl.getCost();
            log.info("직장부터 매물까지 소요시간 : {}", duration2);
            complexTimeDtos.add(
                    ComplexTimeDto.builder()
                            .id(complexId)
                            .duration(Math.toIntExact(duration1 + duration2 + duration3))
                            .build()
            );
        }
        complexTimeDtos.sort(Comparator.comparing(ComplexTimeDto::getDuration));
        return complexTimeDtos.subList(0, 10);
    }

    public List<ComplexTimeDto> getComplexIdsAndDurationsByLocation(String companyId, List<String> complexIds){
        Company company = companyRepository.findById(Long.parseLong(companyId)).orElseThrow(() -> new IllegalArgumentException("No Company found " + companyId));
        if (company.getNearestNodeTime() == null) {
            throw new IllegalArgumentException("No nearestNodeTime found " + companyId);
        }
        String stNodeId = company.getNearestNodeId();
        Long duration1 = company.getNearestNodeTime();
        log.info("직장부터 가장 가까운 정류장까지 소요시간 : {}", duration1);
        List<ComplexTimeDto> complexTimeDtos = new ArrayList<>();
        for (String complexId : complexIds) {
            RealComplex realComplex = realComplexRepository.findById(Long.parseLong(complexId)).orElseThrow(() -> new IllegalArgumentException("No RealComplex found " + complexId));
            if (realComplex.getNearestNodeTime() == null) {
                throw new IllegalArgumentException("No nearestNodeTime found " + complexId);
            }
            String edNodeId = realComplex.getNearestNodeId();
            log.info("매물부터 가장 가까운 정류장까지 소요시간 : {}", realComplex.getNearestNodeTime());
            Long duration3 = realComplex.getNearestNodeTime();
            ShortestPathLink spl = shortestPathLinkRepository.findByStNodeIdAndEdNodeId(stNodeId, edNodeId).orElseThrow(() -> new IllegalArgumentException("No SPL found " + stNodeId + " -> " + edNodeId));
            Long duration2 = spl.getCost();
            log.info("직장부터 매물까지 소요시간 : {}", duration2);
            complexTimeDtos.add(
                    ComplexTimeDto.builder()
                            .id(Long.parseLong(complexId))
                            .duration(Math.toIntExact(duration1 + duration2 + duration3))
                            .build()
            );
        }
        complexTimeDtos.sort(Comparator.comparing(ComplexTimeDto::getDuration));
        return complexTimeDtos;
    }
}
