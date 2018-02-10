package com.worldwide.practice.rx;

import android.os.Looper;

/**
 * Created by Anand on 2/9/2018.
 */

public class Util {

    public static boolean isNotNullOrEmpty(CharSequence value) {
        return (value != null && value.length() > 0);
    }


    public static boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
