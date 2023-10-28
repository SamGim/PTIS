package com.thewayhome.ptis.batch.job.base;

public abstract class AbstractFinalizeJobTasklet extends BaseTasklet {
    public AbstractFinalizeJobTasklet(String jobName, String jobDate) {
        super(jobName, jobDate, jobName + "_FinalizeJobTasklet");
    }
}
