package com.worldwide.practice.feature;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import timber.log.Timber;

/** Created by Anand on 15-02-2018. */
class RetryWithDelay implements Function<Flowable<Throwable>, Publisher<?>> {

    private long retryDelay;
    private int maxNoOfRetry;
    private int attempt;

    public RetryWithDelay(int maxNoOfRetry, long retryDelay) {
        this.retryDelay = retryDelay;
        this.maxNoOfRetry = maxNoOfRetry;
        attempt = 0;
    }

    @Override
    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) throws Exception {
        return throwableFlowable.flatMap(
                error -> {
                    if (++attempt <= maxNoOfRetry) {
                        Timber.d(
                                "Retrying with ma "
                                        + attempt
                                        + " Interval: "
                                        + attempt * retryDelay);
                        return Flowable.timer(attempt * retryDelay, TimeUnit.MILLISECONDS);
                    }
                  Timber.d("Argh! i give up");
                    return Flowable.error(error);
                });
    }
}
