package com.example.z1229.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.assionhonty.lib.assninegridview.AssNineGridViewAdapter;
import com.assionhonty.lib.assninegridview.ImageInfo;
import com.assionhonty.lib.assninegridview.assImgPreview.AssImgPreviewActivity;

import java.io.Serializable;
import java.util.List;

public class AssPreviewAdapter extends AssNineGridViewAdapter {
    private Context context;
    private List<ImageInfo> imageInfo;

    public AssPreviewAdapter(Context context, List<ImageInfo> imageInfo) {
        super(context, imageInfo);
        this.context = context;
        this.imageInfo = imageInfo;
    }

    @Override
    public void onImageItemClick(Context context, AssNineGridView angv, int index, List<ImageInfo> imageInfo) {
        super.onImageItemClick(context, angv, index, imageInfo);

        Intent intent = new Intent(context, AssImgPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("imageInfo", (Serializable)imageInfo);
        bundle.putInt("currentIndex", index);
        bundle.putBoolean("isSave", false);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
