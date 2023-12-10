package com.thewayhome.ptis.batch.job.b0020;

import com.thewayhome.ptis.batch.job.base.AbstractDoMainLogicTasklet;
import com.thewayhome.ptis.core.entity.ShortestPathLink;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.LinkRepository;
import com.thewayhome.ptis.core.repository.NodeRepository;
import com.thewayhome.ptis.core.repository.ShortestPathLinkRepository;
import com.thewayhome.ptis.core.service.LinkService;
import com.thewayhome.ptis.core.service.NodeService;
import com.thewayhome.ptis.core.vo.NodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Qualifier("B0020DoMainLogicTasklet")
@StepScope
public class B0020DoMainLogicTasklet extends AbstractDoMainLogicTasklet {
    private StepExecution stepExecution;
    private final NodeService nodeService;
    private final LinkService linkService;
//    private final ShortestPathLinkService shortestPathLinkService;
    private final LinkRepository linkRepository;
    private final NodeRepository nodeRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final ShortestPathLinkRepository shortestPathLinkRepository;

    public B0020DoMainLogicTasklet(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            NodeService nodeService,
            LinkService linkService,
//            ShortestPathLinkService shortestPathLinkService,
            NodeRepository nodeRepository,
            IdSequenceRepository idSequenceRepository,
            ShortestPathLinkRepository shortestPathLinkRepository,
            LinkRepository linkRepository
    ) {
        super(jobName, jobDate);
        this.nodeService = nodeService;
        this.linkService = linkService;
//        this.shortestPathLinkService = shortestPathLinkService;
        this.nodeRepository = nodeRepository;
        this.idSequenceRepository = idSequenceRepository;
        this.shortestPathLinkRepository = shortestPathLinkRepository;
        this.linkRepository = linkRepository;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws JobExecutionException {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        String jobName = this.getJobName();
        String jobDate = this.getJobDate();
        String taskletName = this.getTaskletName();

        log.info("[" + taskletName + "] jobName = " + jobName);
        log.info("[" + taskletName + "] jobDate = " + jobDate);



        List<NodeVo> nodeList = nodeService.findAll(jobName);
        int size = nodeList.size();

        String[] calcIdTable = new String[size];
        Long[][] calcCostTable = new Long[size][size];

        for (int i = 0; i < size; i++) {
            calcIdTable[i] = nodeList.get(i).getId();
        }

        log.info("calcTable: {} 1", calcCostTable.length);

        List<Object[]> allMinCost = linkRepository.findAllMinCost();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
//                Optional<Long> minCost = linkService.findMinCostLinkByStNodeAndEdNode(calcIdTable[i], calcIdTable[j]);

//                if (minCost.isPresent()){
//                    calcCostTable[i][j] = minCost.get();
//                } else {
//                    throw new IllegalStateException("Link가 존재하지 않습니다.");
//                }
                for (Object[] minCost : allMinCost) {
                    if (calcIdTable[i].equals(minCost[0]) && calcIdTable[j].equals(minCost[1])) {
                        calcCostTable[i][j] = (Long) minCost[2];
                        break;
                    }
                }
            }
        }

        log.info("calcTable: {} 2", calcCostTable.length);

        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++){
                    if (calcCostTable[i][j] > calcCostTable[i][k] + calcCostTable[k][j]){
                        calcCostTable[i][j] = calcCostTable[i][k] + calcCostTable[k][j];
                        log.info("calcCostTable[{}][{}] = {}", i, j, calcCostTable[i][j]);
                    }
                }
            }
        }

        log.info("calcTable: {} 3", calcCostTable.length);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ShortestPathLink req = ShortestPathLink.builder()
                        .id(null)
                        .stNodeId(calcIdTable[i])
                        .edNodeId(calcIdTable[j])
                        .cost(calcCostTable[i][j])
                        .prevNodeId(calcIdTable[i])
                        .createdAt(LocalDateTime.now())
                        .createdBy(this.getJobName())
                        .updatedAt(LocalDateTime.now())
                        .updatedBy(this.getJobName())
                        .build();

                shortestPathLinkRepository.save(req);
            }
        }

        log.info("calcTable: {} 4", calcCostTable.length);

        return RepeatStatus.FINISHED;
    }
}

