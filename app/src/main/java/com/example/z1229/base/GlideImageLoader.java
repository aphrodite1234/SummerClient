package com.example.z1229.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.bumptech.glide.Glide;
import com.example.z1229.summerclient.R;

public class GlideImageLoader implements AssNineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Glide.with(context).
                load(url).
                placeholder(R.drawable.ic_loading).
                error(R.drawable.ic_loading_error).
                into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String s) {
        return null;
    }
}
