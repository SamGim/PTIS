package com.thewayhome.ptis.batch.job.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.step.tasklet.Tasklet;

@RequiredArgsConstructor
@Getter
public abstract class BaseTasklet implements Tasklet {
    private final String jobName;
    private final String jobDate;
    private final String taskletName;
}
