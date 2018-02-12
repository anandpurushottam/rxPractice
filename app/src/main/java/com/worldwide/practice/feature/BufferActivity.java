package com.worldwide.practice.feature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.jakewharton.rxbinding2.view.RxView;
import com.worldwide.practice.R;
import com.worldwide.practice.adapter.LogAdapter;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

/** Created by Anand on 12-02-2018. */
public class BufferActivity extends AppCompatActivity {
    @BindView(R.id.btnClick)
    Button btnClick;

    @BindView(R.id.logsList)
    ListView logsList;

    Unbinder unbinder;
    Disposable disposable;
    private LogAdapter adapter;
    private ArrayList<String> logs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buffer_buuton_layout);
        unbinder = ButterKnife.bind(this);
        setupLogger();
        disposable =
                RxView.clicks(btnClick)
                        .map(
                                onClick -> {
                                    Timber.d("--------- GOT A TAP");
                                    log("GOT A TAP");
                                    return 1;
                                })
                        .subscribeOn(Schedulers.io())
                        .buffer(2, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableObserver<List<Integer>>() {
                                    @Override
                                    public void onNext(List<Integer> integers) {
                                        Timber.d("--------- onNext");
                                        if (integers.size() > 0) {
                                            log(String.format("%d taps", integers.size()));
                                        } else {
                                            Timber.d("--------- No taps received ");
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Timber.e(e, "--------- Woops on error!");
                                        log("Dang error! check your logs");
                                    }

                                    @Override
                                    public void onComplete() {
                                        Timber.d("----- onCompleted");
                                    }
                                });
    }

    private void setupLogger() {
        logs = new ArrayList<>();
        adapter = new LogAdapter(this, logs);
        logsList.setAdapter(adapter);
    }

    private void log(String logMsg) {
        logs.add(0, logMsg + " (main thread) ");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.dispose();
    }
}
