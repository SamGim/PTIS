package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.CompanyRequestDto;
import com.thewayhome.ptis.core.entity.Company;
import com.thewayhome.ptis.core.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    public void saveCompany(CompanyRequestDto companyRequestDto) {
        Company company = Company.builder()
                .companyId(companyRequestDto.getId())
                .companyName(companyRequestDto.getCompanyName())
                .address(companyRequestDto.getAddress())
                .latitude(companyRequestDto.getLatitude())
                .longitude(companyRequestDto.getLongitude())
                .build();
        companyRepository.save(company);
    }
}
