package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.CompanyRequestDto;
import com.thewayhome.ptis.core.dto.response.BusStationQueryResponseDto;
import com.thewayhome.ptis.core.entity.Company;
import com.thewayhome.ptis.core.entity.Node;
import com.thewayhome.ptis.core.repository.CompanyRepository;
import com.thewayhome.ptis.core.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final BatchService batchService;
    private final NodeRepository nodeRepository;
    @Autowired
    public CompanyService(CompanyRepository companyRepository, BatchService batchService, NodeRepository nodeRepository) {
        this.companyRepository = companyRepository;
        this.batchService = batchService;
        this.nodeRepository = nodeRepository;
    }

    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    public void saveCompany(CompanyRequestDto companyRequestDto) {
        BusStationQueryResponseDto busStationQueryResponseDto = batchService.queryNearestBusStationInfo(companyRequestDto.getLatitude().toString(), companyRequestDto.getLongitude().toString(), "1000");
        Node node = nodeRepository.findByNodeSrcId(busStationQueryResponseDto.getBusStationId()).orElseThrow(() -> new IllegalArgumentException("해당 정류장 정보가 존재하지 않습니다."));
        Company company = Company.builder()
                .companyId(companyRequestDto.getId())
                .companyName(companyRequestDto.getCompanyName())
                .address(companyRequestDto.getAddress())
                .latitude(companyRequestDto.getLatitude())
                .longitude(companyRequestDto.getLongitude())
                .nearestNodeId(node.getId())
                .nearestNodeTime(busStationQueryResponseDto.getDuration().longValue())
                .build();
        companyRepository.save(company);
    }
}
