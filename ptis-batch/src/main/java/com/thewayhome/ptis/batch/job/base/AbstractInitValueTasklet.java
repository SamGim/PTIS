package com.thewayhome.ptis.batch.job.base;

public abstract class AbstractInitValueTasklet extends BaseTasklet {
    public AbstractInitValueTasklet(String jobName, String jobDate) {
        super(jobName, jobDate, jobName + "_InitValueTasklet");
    }
}
