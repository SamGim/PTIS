package com.thewayhome.ptis.batch.job.b0021;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.batch.util.GeoUtils;
import com.thewayhome.ptis.core.dto.request.NearbyBusStationOfRealComplexRegisterRequestDto;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.entity.complex.RealComplex;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.ParamService;
import com.thewayhome.ptis.core.service.RealComplexService;
import com.thewayhome.ptis.core.util.APIConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Slf4j
@Component
@Qualifier("B0021DoMainLogicItemProcessor")
@StepScope
public class B0021DoMainLogicItemProcessor implements ItemProcessor<B0021DoMainLogicItemInput, B0021DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private final String stInqDt;
    private final String edInqDt;
    private final String walkSpeed;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;
    private RealComplexService realComplexService;
    private BusStationService busStationService;
    public B0021DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            @Value("#{jobParameters[stInqDt]}") String stInqDt,
            @Value("#{jobParameters[edInqDt]}") String edInqDt,
            @Value("#{jobParameters[walkSpeed]}") String walkSpeed,
            ParamService paramService,
            ObjectMapper objectMapper,
            RealComplexService realComplexService,
            BusStationService busStationService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.stInqDt = stInqDt == null ? "00000101" : stInqDt;
        this.edInqDt = edInqDt == null ? "99991231" : edInqDt;
        this.walkSpeed = walkSpeed == null ? "4" : walkSpeed;
        this.paramService = paramService;
        this.objectMapper = objectMapper;
        this.realComplexService = realComplexService;
        this.busStationService = busStationService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
    @Override
    public B0021DoMainLogicItemOutput process(B0021DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        final Long readComplexId = input.getReadComplexId();

        Optional<RealComplex> realComplexOpt = realComplexService.findById(input.getReadComplexId());
        RealComplex realComplex = null;

        if (realComplexOpt.isEmpty()) {
            return null;
        }
        else
        {
            realComplex = realComplexOpt.get();
        }

        Optional<Param> jobOptional = paramService.getBatchJobInputParam(jobName);
        if (jobOptional.isEmpty()) {
            throw new NoSuchJobException("No such Job Param exists. Jobname: [" + jobName + "]");
        }
        Param param = jobOptional.get();
        String[] paramList = param.getValue().split("\\|");
        int paramIdx = 0;

        String endpoint = paramList[paramIdx++];
        String path = paramList[paramIdx++];

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("tmX", realComplex.getLatitude().toString());
        queryParams.add("tmY", realComplex.getLongitude().toString());
        queryParams.add("radius", "10000");

        log.info("### B0021 Processor :: API 호출 시작 ###");
        String dataFromAPI = APIConnector.getDataFromAPI(endpoint, path, queryParams).block();
        log.info("### B0021 Processor :: API 호출 종료 ###");

        NearbyBusStationOfRealComplexRegisterRequestDto req = null;

        if (dataFromAPI == null) {
            log.info("### B0021 Processor :: API 호출 결과 - 실패 ###");

//            log.info("### B0021 Processor :: 버스노선 수집상태코드 99 변경 처리 ###");
//            stationProcessReq.setBusRouteGatheringStatusCode("99");
        } else {
            log.info("### B0021 Processor :: API 호출 결과 - 성공 ###");

            try {
                JsonNode rootNode = objectMapper.readTree(dataFromAPI);

                log.info("### B0021 Processor :: 버스노선 내 버스정류장 목록 JSON 파싱 시작 ###");
                for (JsonNode item : rootNode) {
                    String busStationArsId = item.get("arsId").asText();
                    String busStationId = item.get("stationId").asText();
                    String busStationDist = item.get("dist").asText();

                    long lnWalkSpeed = Long.parseLong(this.walkSpeed);

                    long busStationCost = GeoUtils.calculateTime(Long.parseLong(busStationDist), lnWalkSpeed);

                    if (busStationId == null || busStationId.isEmpty()) {
                        continue;
                    }

                    Optional<BusStation> busStationOpt = this.busStationService.findByBusStationId(busStationArsId);
                    BusStation busStation = null;

                    if (busStationOpt.isEmpty()) {
                        continue;
                    }
                    else
                    {
                        busStation = busStationOpt.get();
                    }

                    req = NearbyBusStationOfRealComplexRegisterRequestDto.builder()
                            .realComplexId(input.getReadComplexId())
                            .nearBusStationId(busStation.getId())
                            .nearBusStationCost(busStationCost)
                            .nearBusStationMoveType("W")
                            .operatorId(jobName)
                            .build();

                    break;
                }
                log.info("### B0021 Processor :: 버스노선 내 버스정류장 목록 JSON 파싱 종료 ###");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (req == null) {
            return null;
        }
        
        return B0021DoMainLogicItemOutput.builder()
                .realComplexId(input.getReadComplexId())
                .nearNodeId(req.getNearBusStationId())
                .nearNodeCost(req.getNearBusStationCost())
                .nearNodeMoveType(req.getNearBusStationMoveType())
                .build();
    }
}
