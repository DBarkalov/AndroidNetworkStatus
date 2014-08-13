package com.diolan.netstat;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class NetStatActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_log);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NetStatFragment())
                    .commit();
        }
        setTitle(getResources().getString(R.string.netstat_activity_title));
    }



}
