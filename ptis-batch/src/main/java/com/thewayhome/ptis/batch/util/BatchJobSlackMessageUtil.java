package com.thewayhome.ptis.batch.util;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BatchJobSlackMessageUtil {
    private final SlackMessageConnector slackMessageConnector;
    public void emitBatchJobStatusChangedEvent(
            String jobName,
            String jobDate,
            BatchStatus status
    ) {
        final String eventMessage;
        switch (status) {
            case STARTING, STARTED -> eventMessage = "시작됨";
            case STOPPING, STOPPED -> eventMessage = "중지됨";
            case FAILED -> eventMessage = "실패";
            case COMPLETED -> eventMessage = "완료됨";
            case ABANDONED -> eventMessage = "재시도 후 실패";
            case UNKNOWN -> eventMessage = "알 수 없음";
            default -> eventMessage = "";
        }

        StringBuilder slackMessage = new StringBuilder();
        slackMessage.append("[배치 작업 이벤트 발생]").append(System.lineSeparator());
        slackMessage.append(" → 작업 이름: ").append(jobName).append(System.lineSeparator());
        slackMessage.append(" → 작업 일자: ").append(jobDate).append(System.lineSeparator());
        slackMessage.append(" → 작업 상태: ").append(eventMessage).append(System.lineSeparator());

        this.slackMessageConnector.sendSlackMessage(slackMessage.toString());
    }

}
