package com.worldwide.practice.network;

import com.worldwide.practice.data.model.Contributor;
import com.worldwide.practice.data.model.User;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;

/** Created by Anand on 10-02-2018. */
public interface GitHubApi {
    @GET("/users/{user}")
    Observable<User> getUser(@Path("user") String user);

    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(
            @Path("owner") String owner, @Path("repo") String repo);
}
