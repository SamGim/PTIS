package com.thewayhome.ptis.batch.job.base;

public abstract class AbstractFailureHandlingTasklet extends BaseTasklet {
    public AbstractFailureHandlingTasklet(String jobName, String jobDate) {
        super(jobName, jobDate, jobName + "_FailureHandlingTasklet");
    }
}
