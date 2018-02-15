package com.worldwide.practice.feature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.worldwide.practice.adapter.LogAdapter;
import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.worldwide.practice.Util.MATCH_PARENT;
import static com.worldwide.practice.Util.WRAP_CONTENT;

/** Created by Anand on 2/15/2018. */
public class ExponentialBackoffActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> logs;
    private LogAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        listview = new ListView(this);
        listview.setLayoutParams(layoutParams);
        setUpAdapter();
        setContentView(listview);

        PublishProcessor<String> publishProcessor = PublishProcessor.create();
        publishProcessor.onNext("1");
        publishProcessor.onNext("2");
        publishProcessor.onError(new Exception("Somemthing exploded"));

        publishProcessor
                .retry(12)
                .subscribeWith(
                        new DisposableSubscriber<String>() {
                            @Override
                            public void onNext(String log) {
                                updateUi(log);
                            }

                            @Override
                            public void onError(Throwable t) {
                                updateUi(t.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                updateUi("onComplete");
                            }
                        });
    }

    private void setUpAdapter() {
        logs = new ArrayList<>();
        adapter = new LogAdapter(this, logs);
        listview.setAdapter(adapter);
    }

    private void updateUi(String log) {
        logs.add(0, log);
        adapter.notifyDataSetChanged();
    }
}
