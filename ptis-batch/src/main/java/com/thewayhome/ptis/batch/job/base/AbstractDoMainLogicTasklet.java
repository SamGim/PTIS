package com.thewayhome.ptis.batch.job.base;

public abstract class AbstractDoMainLogicTasklet extends BaseTasklet {
    public AbstractDoMainLogicTasklet(String jobName, String jobDate) {
        super(jobName, jobDate, jobName + "_DoMainLogicTasklet");
    }
}
