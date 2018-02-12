package com.worldwide.practice.feature;

import android.os.Bundle;
import android.support.annotation.ColorLong;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.worldwide.practice.R;
import com.worldwide.practice.adapter.LogAdapter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.observable.ObservableJust;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

/** Created by Anand on 13-02-2018. */
public class PollingDataActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;

    Unbinder unbinder;
    List<String> logs;
    LogAdapter adapter;
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polloing_data);

        unbinder = ButterKnife.bind(this);

        setAdapter();
    }

    private void startPolling() {
        disposable =
                Observable.just(1L)
                        .repeatWhen(o -> Observable.interval(2, TimeUnit.SECONDS))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getDisposable());
    }

    private DisposableObserver<Long> getDisposable() {

        return new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                updateLog("Called: " + System.currentTimeMillis());
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        };
    }

    private void setAdapter() {
        logs = new ArrayList<>();
        adapter = new LogAdapter(this, logs);
        listView.setAdapter(adapter);
    }

    private void updateLog(String log) {
        logs.add(0, log);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPolling();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
