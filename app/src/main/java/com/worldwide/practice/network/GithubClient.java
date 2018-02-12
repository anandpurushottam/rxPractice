package com.worldwide.practice.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/** Created by Anand on 10-02-2018. */
public class GithubClient {
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit != null) return retrofit;
        else
            return new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
    }
}
