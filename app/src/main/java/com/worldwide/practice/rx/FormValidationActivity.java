package com.worldwide.practice.rx;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;


import com.jakewharton.rxbinding2.widget.RxTextView;
import com.worldwide.practice.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

/**
 * Created by Anand on 10-02-2018.
 */

public class FormValidationActivity extends AppCompatActivity {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etAge)
    EditText etAge;
    @BindView(R.id.btnDone)
    Button btnDone;

    private Unbinder unbinder;

    private Disposable disposable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_validation_layout);
        unbinder = ButterKnife.bind(this);


        Flowable<CharSequence> nameObservable = RxTextView.textChanges(etName)
                .skip(1)
                .toFlowable(BackpressureStrategy.LATEST);
        Flowable<CharSequence> emailObservable = RxTextView.textChanges(etEmail)
                .skip(1)
                .toFlowable(BackpressureStrategy.LATEST);
        Flowable<CharSequence> ageObservable = RxTextView.textChanges(etAge)
                .skip(1)
                .toFlowable(BackpressureStrategy.LATEST);


        disposable = Flowable.combineLatest(nameObservable, emailObservable, ageObservable,
                (name, email, age) -> {
                    boolean isValidName = Util.isNotNullOrEmpty(name);
                    if (!isValidName) {
                        etName.setError("Invalid name");
                    }
                    boolean isValidEmail = (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
                    if (!isValidEmail) {
                        etEmail.setError("Invalid email");
                    }
                    boolean isValidAge = Util.isNotNullOrEmpty(age) && Integer.parseInt(age.toString()) > 18;
                    if (!isValidAge) {
                        etAge.setError("Invalid age");
                    }

                    return isValidName && isValidEmail && isValidAge;
                }).subscribeWith(getDisposableSubscriber());


    }

    public DisposableSubscriber<Boolean> getDisposableSubscriber() {
        return new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean isEnabled) {
                btnDone.setEnabled(isEnabled);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.dispose();
    }
}
