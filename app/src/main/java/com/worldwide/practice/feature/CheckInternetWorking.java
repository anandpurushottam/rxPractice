package com.worldwide.practice.feature;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;
import static com.worldwide.practice.Util.*;
/** Created by Anand on 2/14/2018. */
public class CheckInternetWorking extends AppCompatActivity {
    private TextView tvResult;
    Disposable disposable;

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
    }

    private void checkInternetConnection() {
        disposable =
                Observable.interval(1000, TimeUnit.MILLISECONDS)
                        .map(o -> isNetworkAvailable())
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getDisposableObservable());
    }

    private DisposableObserver<Boolean> getDisposableObservable() {
        return new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                updateUI(aBoolean);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
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
        disposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
