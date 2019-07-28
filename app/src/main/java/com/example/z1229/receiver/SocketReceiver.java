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

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
