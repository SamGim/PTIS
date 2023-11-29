package com.thewayhome.ptis.batch.job.b0014;

import lombok.Getter;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

@Getter
@Component
public class B0014DoMainLogicRetryTemplate {
    private static final int MAX_ATTEMPTS = 2;
    private static final long BACK_OFF_PERIOD = 10000;

    public RetryPolicy retryPolicy() {
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(MAX_ATTEMPTS);
        return policy;
    }

    public BackOffPolicy backOffPolicy() {
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(BACK_OFF_PERIOD);
        return policy;
    }
}
