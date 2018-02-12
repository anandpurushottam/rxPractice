package com.worldwide.practice.feature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.worldwide.practice.R;
import com.worldwide.practice.adapter.LogAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

import static com.worldwide.practice.Util.isNotNullOrEmpty;

/** Created by Anand on 2/9/2018. */
public class AutoCompleteActivity extends AppCompatActivity {
    @BindView(R.id.searchField)
    EditText searchField;

    @BindView(R.id.listView)
    ListView listView;

    Unbinder unbinder;
    LogAdapter adapter;
    List<String> items;
    DisposableObserver<String> disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        setAdapter();

        disposable =
                RxTextView.textChangeEvents(searchField)
                        .debounce(450, TimeUnit.MILLISECONDS)
                        .filter(v -> isNotNullOrEmpty(v.text()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(o -> o.text().toString())
                        .subscribeWith(getObserver());
    }

    private DisposableObserver<String> getObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                logValue(s);
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

    private void setAdapter() {
        items = new ArrayList<>();
        adapter = new LogAdapter(this, items);
        listView.setAdapter(adapter);
    }

    private void logValue(CharSequence text) {
        items.add(0, text.toString());
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btnClear)
    public void clearList() {
        items.clear();
        adapter.notifyDataSetChanged();
        searchField.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.dispose();
    }
}
