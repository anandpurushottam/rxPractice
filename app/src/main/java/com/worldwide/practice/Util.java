package com.worldwide.practice;

import android.content.res.Resources;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

/** Created by Anand on 2/9/2018. */
public class Util {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static boolean isNotNullOrEmpty(CharSequence value) {
        return (value != null && value.length() > 0);
    }

    public static boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static int inDp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }
}
