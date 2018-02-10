package com.worldwide.practice;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by Anand on 2/9/2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Timber.plant(new Timber.DebugTree());
    }
}
