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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        // nodeList의 인덱스를 calcCostTable의 인덱스로 그대로 쓴다.
        // 즉 calcCostTable[i]는 nodeList[i]의 노드이다.
        // 인덱스를 통해 id를 가져오는것은 가능하지만 id를 통해서 인덱스를 찾는게 불가능하기에 map을 하나 만들어준다.
        List<NodeVo> nodeList = nodeService.findAll(jobName);
        int size = nodeList.size();


        Long[][] calcCostTable = new Long[size][size];
        Integer[][] calcPrevTable = new Integer[size][size];
        String[][] prevLinkIdTable = new String[size][size];

//        for (int i = 0; i < size; i++) {
//            calcIdTable[i] = nodeList.get(i).getId();
//        }

        log.info("시작");
        // 시간 측정을 위한 변수 선언
        long startTime = System.currentTimeMillis();

        // 노드간의 최단 링크만을 전부 가져온다.
        // l.stNode.id, l.edNode.id, l.cost, l.linkId
        List<Object[]> allMinCost = linkRepository.findAllMinCostLinks();


        // nodeId를 이용해서 인덱스를 찾는 map을 만든다.
        Map<String, Integer> idIndexMap = new HashMap<>();
        for (int i = 0; i < nodeList.size(); i++) {
            idIndexMap.put(nodeList.get(i).getId(), i);
        }
        log.info("MinCostLink 개수 : {}, node * node : {}", allMinCost.size(), size * size);
        // 반대로 allMinCost를 하나씩 가져와서 i->j 테이블에 넣는다. (최단시간테이블 초기화)
        // 단 allMinCost의 개수가 노드 * 노드 개수와 같아야만 한다. 매우 중요
        for (Object[] minCost : allMinCost){
            Integer curStNodeIndex = idIndexMap.get((String) minCost[0]);
            Integer curEdNodeIndex = idIndexMap.get((String) minCost[1]);
            calcCostTable[curStNodeIndex][curEdNodeIndex] = (Long) minCost[2];
            prevLinkIdTable[curStNodeIndex][curEdNodeIndex] = (String) minCost[3];
        }

        // prevTable 초기화
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                calcPrevTable[i][j] = i;
                log.info("테이블 초기화중 {}%", (double)(i * size + j) / (double)(size * size) * 100);
            }
        }

        log.info("테이블 초기화 끝, 플로이드 시작");
        long checkPoint1 = System.currentTimeMillis();
        log.info("플로이드 초기화 시간 : {}", checkPoint1 - startTime);


        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
//                log.info("i = {}, k = {}", i, k);
                if (i == k) continue;
                for (int j = 0; j < size; j++){
                    if (calcCostTable[i][j] > calcCostTable[i][k] + calcCostTable[k][j]){
                        calcCostTable[i][j] = calcCostTable[i][k] + calcCostTable[k][j];
                        calcPrevTable[i][j] = k;
                        prevLinkIdTable[i][j] = prevLinkIdTable[i][k];
                        log.info("플로이드-워셜 진행중 {}%", (double)(i * size + j) / (double)(size * size) * 100);
                    }
                }
            }
        }

        log.info("플로이드 끝, 테이블 -> SPL 변환 시작");
        long checkPoint2 = System.currentTimeMillis();
        log.info("플로이드 시간 : {}", checkPoint2 - checkPoint1);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int prevNodeIndex = calcPrevTable[i][j];
                ShortestPathLink req = ShortestPathLink.builder()
                        .id(null)
                        .stNodeId(nodeList.get(i).getId())
                        .stNodeType(nodeList.get(i).getNodeSrcType())
                        .edNodeId(nodeList.get(j).getId())
                        .edNodeType(nodeList.get(j).getNodeSrcType())
                        .cost(calcCostTable[i][j])
                        .prevNodeId(nodeList.get(prevNodeIndex).getId())
                        .linkId(prevLinkIdTable[i][j])
                        .createdAt(LocalDateTime.now())
                        .createdBy(this.getJobName())
                        .updatedAt(LocalDateTime.now())
                        .updatedBy(this.getJobName())
                        .build();

                shortestPathLinkRepository.save(req);
                log.info("디비에 SPL 저장중 : {}%", (double)(i * size + j) / (double)(size * size) * 100);
            }
        }

        log.info("플로이드 끝");
        long checkPoint3 = System.currentTimeMillis();
        log.info("SPL 저장 시간 : {}", checkPoint3 - checkPoint2);

        return RepeatStatus.FINISHED;
    }
}

