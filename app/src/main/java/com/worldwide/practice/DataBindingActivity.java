package com.worldwide.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.worldwide.practice.rx.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

/**
 * Created by Anand on 2/12/2018.
 */

public class DataBindingActivity extends AppCompatActivity {

    @BindView(R.id.tvResult)
    TextView tvResult;

    @BindView(R.id.etFirst)
    EditText etFirst;

    @BindView(R.id.etLast)
    EditText etLast;

    Unbinder unbinder;

    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_binding);
        unbinder = ButterKnife.bind(this);

        Flowable<Integer> first = RxTextView.textChanges(etFirst).toFlowable(BackpressureStrategy.LATEST).map(v -> {
            if (Util.isNotNullOrEmpty(v)) {
                return Integer.parseInt(v.toString());
            } else return 0;
        });
        Flowable<Integer> last = RxTextView.textChanges(etLast).toFlowable(BackpressureStrategy.LATEST).map(v -> {
            if (Util.isNotNullOrEmpty(v)) {
                return Integer.parseInt(v.toString());
            } else return 0;
        });
        //Disposable
        disposable = Flowable.combineLatest(first, last, (x, y) -> x + y)
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        updateUi(String.valueOf(integer));
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e(t);

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Completed");
                    }
                });

    }

    private void updateUi(String sum) {
        tvResult.setText("Sum: " + sum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.dispose();

    }
}
