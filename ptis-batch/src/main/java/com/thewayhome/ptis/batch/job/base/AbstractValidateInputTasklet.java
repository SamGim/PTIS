package com.thewayhome.ptis.batch.job.base;

public abstract class AbstractValidateInputTasklet extends BaseTasklet {
    public AbstractValidateInputTasklet(String jobName, String jobDate) {
        super(jobName, jobDate, jobName + "_ValidateInputTasklet");
    }
}
