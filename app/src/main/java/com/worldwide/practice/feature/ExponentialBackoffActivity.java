package com.worldwide.practice.feature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ListView;
import com.worldwide.practice.adapter.LogAdapter;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

import static com.worldwide.practice.Util.MATCH_PARENT;

/** Created by Anand on 2/15/2018. */
public class ExponentialBackoffActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> logs;
    private LogAdapter adapter;
    public final int MAX_RETRY = 10;
    public final long MINIMUM_DELAY = 1000;
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        listview = new ListView(this);
        listview.setLayoutParams(layoutParams);

        setContentView(listview);
        setUpAdapter();
        RetryWithDelay retryWithDelay = new RetryWithDelay(MAX_RETRY, MINIMUM_DELAY);
        disposable =
                Flowable.error(new RuntimeException("Some error occurred"))
                        .retryWhen(retryWithDelay)
                        .subscribeWith(
                                new DisposableSubscriber<Object>() {
                                    @Override
                                    public void onNext(Object o) {

                                        Timber.d("Onnext");
                                        updateUi("OnNext");
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        updateUi(t.getLocalizedMessage());
                                        Timber.d(t.getLocalizedMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        updateUi("OnNext");
                                        Timber.d("onComplete");
                                    }
                                });
    }

    private void setUpAdapter() {
        logs = new ArrayList<>();
        adapter = new LogAdapter(this, logs);
        listview.setAdapter(adapter);
    }

    private void updateUi(String log) {
        Timber.d(log);
        logs.add(0, log);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
