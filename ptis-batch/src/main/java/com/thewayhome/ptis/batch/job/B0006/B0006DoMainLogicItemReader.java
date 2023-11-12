package com.thewayhome.ptis.batch.job.B0006;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thewayhome.ptis.core.service.ParamService;
import com.thewayhome.ptis.batch.util.CommonUtils;
import com.thewayhome.ptis.core.entity.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
@Qualifier("B0006DoMainLogicItemReader")
@StepScope
public class B0006DoMainLogicItemReader implements ItemStreamReader<B0006DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private final String type;
    private final String filename;
    private List<Map<String, Object>> jArray;
    private int idx = 0;

    public B0006DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            ParamService paramService
    ) throws IndexOutOfBoundsException, NoSuchJobException, IOException {
        this.jobName = jobName;
        this.jobDate = jobDate;

        Param param = paramService.getBatchJobInputParam(jobName)
                .orElseThrow(() -> new NoSuchJobException("No such Job Param exists. Jobname: [" + jobName + "]"));

        String[] paramList = param.getValue().split("\\|");
        int paramIdx = 0;

        this.type = paramList[paramIdx++];
        this.filename = paramList[paramIdx++];

        initialize();
    }

    public void initialize() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(this.filename);
            InputStream inputStream = resource.getInputStream();
            String jString = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            this.jArray = getJsonArray(jString);
        } catch (IOException e) {
            // 파일 읽기 중 오류 발생
            e.printStackTrace();
            throw new IOException("File read error: " + this.filename);
        }
    }



    private List<Map<String, Object>> getJsonArray(String jsonString) throws JsonProcessingException {
        if ("J".equals(this.type)){
            Map<String, Object> jsonMap = CommonUtils.convertJSONstringToMap(jsonString);
            List<Map<String, Object>> jArray = (List<Map<String, Object>>) jsonMap.get("DATA");
            if (jArray == null || jArray.size() == 0) {
                throw new IllegalArgumentException("Invalid json string: " + jsonString);
            }
            return jArray;
        }
        else {
            throw new IllegalArgumentException("Invalid type: " + this.type);
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.idx = executionContext.getInt("READ_IDX", 0);
        log.info("READ_IDX: " + this.idx);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put("READ_IDX", this.idx);
        log.info("UPDATE_IDX : " + this.idx);
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("CLOSE_IDX : " + this.idx);
    }

    @Override
    public B0006DoMainLogicItemInput read() {
        if (this.idx >= this.jArray.size()) {
            return null;
        }

        Map<String, Object> item = this.jArray.get(this.idx++);

        while (Objects.equals((String) item.get("dtlstatenm"), "폐업")){
            if (this.idx >= this.jArray.size()) {
                return null;
            }
            item = this.jArray.get(this.idx++);
        }
        String nodeId = (String) item.get("mgtno");
        String nodeName = (String) item.get("bplcnm");
        String nodeAddress = (String) item.get("sitewhladdr");
        String nodePosX = (String) item.get("x");
        String nodePosY = (String) item.get("y");


        return B0006DoMainLogicItemInput
                .builder()
                .nodeId(nodeId)
                .nodeAddress(nodeAddress)
                .nodePosX(nodePosX)
                .nodePosY(nodePosY)
                .nodeName(nodeName)
                .build();
    }
}
