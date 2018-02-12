package com.worldwide.practice.feature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.jakewharton.rxbinding2.view.RxView;
import com.worldwide.practice.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import timber.log.Timber;

/** Created by Anand on 12-02-2018. */
public class ConcurrencyWithSchedulersDemoActivity extends AppCompatActivity {
    @BindView(R.id.btnClick)
    Button btnClick;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Unbinder unbinder;
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concurrency_schedulers);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btnClick)
    public void perform() {
        progressBar.setVisibility(View.VISIBLE);

        disposable =
                Observable.just(true)
                        .map(
                                aBoolean -> {
                                    performLongRunningTaskOnCurrentThread();
                                    return aBoolean;
                                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableObserver<Boolean>() {
                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        Timber.d("Onnext: " + aBoolean);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Timber.d(e);
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onComplete() {
                                        operatonCompleted();
                                    }
                                });
    }

    private void operatonCompleted() {
        Timber.d("Completed");
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
    }

    public void performLongRunningTaskOnCurrentThread() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        unbinder.unbind();
    }
}
