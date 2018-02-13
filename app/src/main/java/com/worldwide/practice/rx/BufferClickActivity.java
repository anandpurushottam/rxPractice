package com.worldwide.practice.rx;

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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

/** Created by Anand on 2/12/2018. */
public class BufferClickActivity extends AppCompatActivity {
    @BindView(R.id.btnTap)
    Button btnTap;

    @BindView(R.id.listView)
    ListView listView;

    List<String> logs;
    LogAdapter adapter;
    Unbinder unbinder;
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buffer_calls);
        unbinder = ButterKnife.bind(this);
        setAdapter();
    }

    private Disposable getDisposableObservable() {
        return RxView.clicks(btnTap)
                .map(
                        clickEvent -> {
                            logChanges("Button Tapped");
                            return 1;
                        })
                .buffer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<List<Integer>>() {
                            @Override
                            public void onNext(List<Integer> integers) {
                                if (integers.size() > 0) {
                                    logChanges("Button Tapped : " + integers.size() + " times");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {}

                            @Override
                            public void onComplete() {
                                logChanges("Completed");
                            }
                        });
    }

    private void logChanges(String log) {
        Timber.d(log);
        logs.add(0, log);
        adapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        logs = new ArrayList<>();

        adapter = new LogAdapter(this, logs);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disposable = getDisposableObservable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.dispose();
    }
}
