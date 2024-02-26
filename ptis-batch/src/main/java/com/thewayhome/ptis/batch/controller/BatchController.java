package com.thewayhome.ptis.batch.controller;

import com.thewayhome.ptis.batch.util.BatchJobManipulateUtil;
import com.thewayhome.ptis.core.dto.request.CompanyRequestDto;
import com.thewayhome.ptis.core.dto.request.RealComplexRequestDto;
import com.thewayhome.ptis.core.dto.request.SPLResponseDto;
import com.thewayhome.ptis.core.dto.response.ComplexTimeDto;
import com.thewayhome.ptis.core.service.CompanyService;
import com.thewayhome.ptis.core.service.RealComplexService;
import com.thewayhome.ptis.core.service.ShortestPathLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class BatchController {

    @Autowired
    private BatchJobManipulateUtil batchJobManipulateUtil;
    @Autowired
    private ShortestPathLinkService shortestPathLinkService;
    @Autowired
    private RealComplexService realComplexService;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobLauncher jobLauncher;

    @PostMapping("/batch/start")
    public String startBatchJob(
            @RequestParam String jobName,
            @RequestParam String jobDate,
            @RequestBody(required = false) Map<String, String> jobParamReq
    ) {
        JobParametersBuilder jobParamBuilder = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("jobName", jobName)
                .addString("jobDate", jobDate);

        if (jobParamReq != null) {
            for (Map.Entry<String, String> entry : jobParamReq.entrySet()) {
                jobParamBuilder.addString(entry.getKey(), entry.getValue());
            }
        }

        try {
            log.warn(jobParamBuilder.toJobParameters().toString());
            batchJobManipulateUtil.launchJob(jobName, jobParamBuilder.toJobParameters());
        } catch (Exception e) {
            return "Error starting the batch job " + jobName + ". " + e.getMessage();
        }

        return "Batch job " + jobName +" has been started.";
    }

    @PostMapping("/batch/stop")
    public String stopBatchJob(
            @RequestParam String jobName
    ) {
        batchJobManipulateUtil.stopJobExecution(jobName);
        return "Batch job " + jobName +" has been stopped.";
    }

    @GetMapping("/spl")
    public Map<String, List<SPLResponseDto>> getSPL(
            @RequestParam String srcNodeName,
            @RequestParam String destNodeName
    ) {
        Map<String, List<SPLResponseDto>> result = new HashMap<>();
        // param으로 받은 String을 Long 타입으로 변환

        result.put("items", shortestPathLinkService.getSplFull(Long.parseLong(srcNodeName), Long.parseLong(destNodeName)));
        return result;
    }

    @GetMapping("/complex/company/{companyId}")
    public Map<String, List<ComplexTimeDto>> getComplexIdsAndDurationByCompanyId(
            @PathVariable String companyId
    ){
        Map<String, List<ComplexTimeDto>> result = new HashMap<>();
        result.put("items", shortestPathLinkService.getComplexIdsAndDuration(companyId));
        return result;
    }

    @GetMapping("/complex/location/{companyId}")
    public Map<String, List<ComplexTimeDto>> getComplexIdsAndDurationByLocation(
            @PathVariable String companyId,
            @RequestBody List<String> complexIds

    ){
        Map<String, List<ComplexTimeDto>> result = new HashMap<>();
        result.put("items", shortestPathLinkService.getComplexIdsAndDurationsByLocation(companyId, complexIds));
        return result;
    }

    @PostMapping("/complex/upload")
    public ResponseEntity<?> uploadComplexData(
            @RequestBody RealComplexRequestDto realComplexRequestDto
            ) {
        realComplexService.saveRealComplex(realComplexRequestDto);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/company/upload")
    public ResponseEntity<?> uploadCompanyData(
            @RequestBody CompanyRequestDto companyRequestDto
    ) {
        companyService.saveCompany(companyRequestDto);
        return ResponseEntity.ok("ok");
    }
}
