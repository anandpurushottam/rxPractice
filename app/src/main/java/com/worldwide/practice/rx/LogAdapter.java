package com.worldwide.practice.rx;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.worldwide.practice.R;

import java.util.List;

class LogAdapter extends ArrayAdapter<String> {

    LogAdapter(Context context, List<String> logs) {
        super(context, R.layout.item_log, R.id.item_log, logs);
    }
}