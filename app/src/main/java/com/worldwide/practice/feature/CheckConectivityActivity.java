package com.worldwide.practice.feature;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;
import static com.worldwide.practice.Util.*;
/** Created by Anand on 2/14/2018. */
public class CheckConectivityActivity extends AppCompatActivity {
    private TextView tvResult;
    Disposable disposable;
    BroadcastReceiver broadcastReceiver;
    PublishProcessor<Boolean> processor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        tvResult = new TextView(this);
        tvResult.setLayoutParams(layoutParams);
        tvResult.setGravity(Gravity.CENTER);
        tvResult.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        setContentView(tvResult);

        init();
    }

    private void init() {
        processor = PublishProcessor.create();

        broadcastReceiver =
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        processor.onNext(isNetworkAvailable());
                    }
                };
    }

    private void register() {
        disposable =
                processor
                        .startWith(isNetworkAvailable())
                        .distinctUntilChanged()
                        .subscribeWith(getDisposableSubscriber());
        registerReceiver(
                broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unregister() {
        unregisterReceiver(broadcastReceiver);
    }

    private DisposableSubscriber<Boolean> getDisposableSubscriber() {
        return new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                updateUI(aBoolean);
            }

            @Override
            public void onError(Throwable t) {
                Timber.e(t);
            }

            @Override
            public void onComplete() {
                Timber.d("Completed");
            }
        };
    }

    private void updateUI(Boolean result) {
        if (result) {
            tvResult.setText("Internet avialable: (:");
        } else {
            tvResult.setText("Opps no working connection:  ):");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
