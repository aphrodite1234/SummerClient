package com.example.z1229.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String ACTION_SOCKET = "com.example.z1229.receiver.socketReceiver";
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SOCKET);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver,intentFilter);
    }

    protected abstract void doAction(String content);

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_SOCKET)) {
                doAction(intent.getStringExtra("text"));
            }
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
    };
}
