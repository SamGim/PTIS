package com.thewayhome.ptis.batch.job.b0001;

import com.mysql.cj.util.StringUtils;
import com.thewayhome.ptis.batch.service.BatchJobService;
import com.thewayhome.ptis.batch.vo.BatchJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("B0001DoMainLogicItemReader")
@StepScope
public class B0001DoMainLogicItemReader implements ItemReader<B0001DoMainLogicItemInput> {
    private final String jobName;
    private final String jobDate;
    private final String type;
    private final String filename;
    private final String delimiter;
    private final List<B0001DoMainLogicItemInput> items;

    public B0001DoMainLogicItemReader(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            BatchJobService batchJobService
    ) throws IOException, IndexOutOfBoundsException, NoSuchJobException {
        this.jobName = jobName;
        this.jobDate = jobDate;

        Optional<BatchJob> jobOptional = batchJobService.findBatchJob(jobName);
        if (jobOptional.isEmpty()) {
            throw new NoSuchJobException("No such Job [" + jobName + "] exists");
        }

        BatchJob batchJob = jobOptional.get();
        this.type = batchJob.getInputType();
        this.filename = batchJob.getInputFilename();
        this.delimiter = batchJob.getInputDelimiter();

        this.items = new ArrayList<>();
        initialize();
    }

    public void initialize() throws IOException, IndexOutOfBoundsException {
        BufferedReader br = getBufferedReader();
        String buf = "";

        while((buf = br.readLine()) != null){
            int tokenIdx = 0;
            List<String> tokenList = StringUtils.split(buf, this.delimiter, true);

            this.items.add(B0001DoMainLogicItemInput
                            .builder()
                            .nodeId(tokenList.get(tokenIdx++))
                            .arsId(tokenList.get(tokenIdx++))
                            .nodeName(tokenList.get(tokenIdx++))
                            .nodePosX(tokenList.get(tokenIdx++))
                            .nodePosY(tokenList.get(tokenIdx++))
                            .nodePosType(tokenList.get(tokenIdx++))
                            .build()
            );
        }
    }

    private BufferedReader getBufferedReader() throws IOException {
        final InputStreamReader inputStreamReader;

        if ("F".equals(this.type)) {
            File inputFile = new File(this.filename);
            InputStream inputStream = new FileInputStream(inputFile);
            inputStreamReader = new InputStreamReader(inputStream);
        } else if ("R".equals(this.type)) {
            ClassPathResource resource = new ClassPathResource(this.filename);
            InputStream inputStream = resource.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
        } else {
            throw new IOException();
        }

        return new BufferedReader(inputStreamReader);
    }

    @Override
    public B0001DoMainLogicItemInput read() {
        return items.isEmpty() ? null : items.remove(0);
    }
}
