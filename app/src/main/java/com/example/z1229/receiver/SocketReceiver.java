package com.example.z1229.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class SocketReceiver extends BroadcastReceiver {

    private Gson gson = new Gson();
    private TextView textView;
    ImageView imageView;
    public static final String ACTION_SOCKET = "com.example.z1229.receiver.SocketReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_SOCKET)) {
            doActionSocket(intent.getStringExtra("text"));
        }
    }

    public void doActionSocket(String string){

    }
}
