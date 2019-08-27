package com.example.z1229.base;

import android.app.Application;

import com.assionhonty.lib.assninegridview.AssNineGridView;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AssNineGridView.setImageLoader(new GlideImageLoader());
    }
}
