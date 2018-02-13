package com.worldwide.practice.rx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.OnTextChanged;
import com.worldwide.practice.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;
import static android.text.TextUtils.isEmpty;
/** Created by Anand on 2/12/2018. */
public class DataBindingActivity extends AppCompatActivity {

    @BindView(R.id.tvResult)
    TextView tvResult;

    @BindView(R.id.etFirst)
    EditText etFirst;

    @BindView(R.id.etLast)
    EditText etLast;

    Unbinder unbinder;

    Disposable disposable;
    BehaviorSubject<Float> resultEmitterSubject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_binding);
        unbinder = ButterKnife.bind(this);

        resultEmitterSubject = BehaviorSubject.create();
        disposable =
                resultEmitterSubject.subscribeWith(
                        new DisposableObserver<Float>() {
                            @Override
                            public void onNext(Float aFloat) {
                                updateUi(String.valueOf(aFloat));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                            }

                            @Override
                            public void onComplete() {
                                Timber.d("Completed");
                            }
                        });
    }

    @OnTextChanged({R.id.etFirst, R.id.etLast})
    public void onNumberChanged() {
        float num1 = 0;
        float num2 = 0;

        if (!isEmpty(etFirst.getText().toString())) {
            num1 = Float.parseFloat(etFirst.getText().toString());
        }

        if (!isEmpty(etLast.getText().toString())) {
            num2 = Float.parseFloat(etLast.getText().toString());
        }

        resultEmitterSubject.onNext(num1 + num2);
    }

    private void updateUi(String sum) {
        tvResult.setText("BehaviorSubject: " + sum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.dispose();
    }
}
