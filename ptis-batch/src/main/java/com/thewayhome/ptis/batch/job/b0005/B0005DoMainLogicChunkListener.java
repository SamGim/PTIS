package com.thewayhome.ptis.batch.job.b0005;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("B0005DoMainLogicChunkListener")
@StepScope
public class B0005DoMainLogicChunkListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();

        log.info("[" + stepExecution.getStepName() + "]" + " Chunk Read #" + stepExecution.getReadCount());
    }

    @Override
    public void afterChunk(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();

        try {
            Thread.sleep(15 * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("[" + stepExecution.getStepName() + "]" + " Chunk Commit #" + stepExecution.getCommitCount());
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();

        log.error("[" + stepExecution.getStepName() + "]" + " Chunk Rollback" + "\n" +
                " >> Read count         : " + stepExecution.getReadCount() + "\n" +
                " >> Write count        : " + stepExecution.getWriteCount() + "\n" +
                " >> Commit count       : " + stepExecution.getCommitCount() + "\n" +
                " >> Rollback count     : " + stepExecution.getRollbackCount() + "\n" +
                " >> Filter count       : " + stepExecution.getFilterCount() + "\n" +
                " >> Skip count         : " + stepExecution.getSkipCount() + "\n" +
                " >> Read Skip count    : " + stepExecution.getReadSkipCount() + "\n" +
                " >> Process Skip count : " + stepExecution.getProcessSkipCount() + "\n" +
                " >> Write Skip count   : " + stepExecution.getWriteSkipCount() + "\n"
        );
    }
}
