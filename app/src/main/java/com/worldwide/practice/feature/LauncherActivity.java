package com.worldwide.practice.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.HashMap;
import java.util.Map;

import static com.worldwide.practice.Util.*;

/** Created by Anand on 10-02-2018. */
public class LauncherActivity extends AppCompatActivity {

    private HashMap<String, Class> map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        setView();
    }

    private void setData() {
        map = new HashMap<>();
        map.put("AutoCompleteActivity", AutoCompleteActivity.class);
        map.put("FormValidationActivity", FormValidationActivity.class);
        map.put("NetworkRequestActivity", NetworkRequestActivity.class);
        map.put(
                "ConcurrencyWithSchedulersDemoActivity",
                ConcurrencyWithSchedulersDemoActivity.class);
        map.put("BufferClickActivity", BufferClickActivity.class);
        map.put("PollingDataActivity", PollingDataActivity.class);
        map.put("CheckConectivityActivity", CheckConectivityActivity.class);
        map.put("ExponentialBackoffActivity", ExponentialBackoffActivity.class);
    }

    private void setView() {
        LinearLayout rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootView.setPadding(inDp(32), inDp(32), inDp(32), inDp(32));

        for (Map.Entry<String, Class> entry : map.entrySet()) {
            String key = entry.getKey();
            Class activity = entry.getValue();
            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

            button.setText(key);
            button.setOnClickListener(view -> gotToActivity(activity));
            rootView.addView(button);
        }

        setContentView(rootView);
    }

    void gotToActivity(Class activity) {
        Intent login = new Intent(this, activity);
        startActivity(login);
    }
}
