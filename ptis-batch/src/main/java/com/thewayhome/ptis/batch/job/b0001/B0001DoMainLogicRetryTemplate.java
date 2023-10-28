package com.thewayhome.ptis.batch.job.b0001;

import lombok.Getter;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.core.step.skip.ExceptionClassifierSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.classify.SubclassClassifier;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

@Component
public class B0001DoMainLogicRetryTemplate {
    private static final int MAX_ATTEMPTS = 5;
    private static final long BACK_OFF_PERIOD = 3000;

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

    public SkipPolicy skipPolicy() {
        ExceptionClassifierSkipPolicy policy = new ExceptionClassifierSkipPolicy();

        SubclassClassifier<Throwable, SkipPolicy> classifier = new SubclassClassifier<>();
        classifier.add(IndexOutOfBoundsException.class, new AlwaysSkipItemSkipPolicy());

        policy.setExceptionClassifier(classifier);

        return policy;
    }
}
