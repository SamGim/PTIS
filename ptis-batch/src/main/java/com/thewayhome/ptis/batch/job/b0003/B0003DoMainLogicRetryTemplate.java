package com.thewayhome.ptis.batch.job.b0003;

import lombok.Getter;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Getter
@Component
public class B0003DoMainLogicRetryTemplate {
    private static final int MAX_ATTEMPTS = 2;
    private static final long BACK_OFF_PERIOD = 15000;

    public RetryPolicy retryPolicy() {
        return new SimpleRetryPolicy(
                MAX_ATTEMPTS,
                Collections.singletonMap(IOException.class, true)
        );
    }

    public BackOffPolicy backOffPolicy() {
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(BACK_OFF_PERIOD);
        return policy;
    }

    public SkipPolicy skipPolicy() {
        return (t, skipCount) -> t instanceof IllegalArgumentException;
    }
}
