package com.worldwide.practice.feature;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.worldwide.practice.R;
import com.worldwide.practice.adapter.GithubUserAdapter;
import com.worldwide.practice.data.model.Contributor;
import com.worldwide.practice.data.model.User;
import com.worldwide.practice.network.GitHubApi;
import com.worldwide.practice.network.GithubClient;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import timber.log.Timber;

/** Created by Anand on 10-02-2018. */
public class NetworkRequestActivity extends AppCompatActivity {
    private DisposableObserver<List<Contributor>> disposable;
    private Context mContext;
    GithubUserAdapter adapter;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.listView)
    ListView listView;

    Unbinder unbinder;

    List<Contributor> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.github_layout);
        unbinder = ButterKnife.bind(this);
        list = new ArrayList<>();
        setAdapter();

        mContext = this;
        Retrofit retrofit = GithubClient.getInstance();
        GitHubApi service = retrofit.create(GitHubApi.class);

        disposable =
                service.contributors("JakeWharton", "butterknife")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableObserver<List<Contributor>>() {
                                    @Override
                                    public void onNext(List<Contributor> contributors) {
                                        Timber.d(contributors.get(0).getLogin());

                                         updateList(contributors);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Timber.e(e, "Error: ");
                                    }

                                    @Override
                                    public void onComplete() {
                                        Timber.d("Completed");
                                    }
                                });
    }

    private void setAdapter() {
        adapter = new GithubUserAdapter(this, list);
        listView.setAdapter(adapter);
    }

    private void updateList(List<Contributor> contributors) {
        list.addAll(contributors);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        unbinder.unbind();
    }
}
