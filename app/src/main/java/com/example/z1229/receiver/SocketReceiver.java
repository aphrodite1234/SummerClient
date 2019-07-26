package com.example.z1229.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class SocketReceiver extends BroadcastReceiver {

    TextView textView;
    ImageView imageView;
    int i=0;

    public SocketReceiver(ImageView textView){
        this.imageView=textView;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //textView.append(i+++"|"+intent.getStringExtra("text")+"\n");
        Gson gson = new Gson();
//        RMessage rMessage = new RMessage();
//        rMessage = gson.fromJson(intent.getStringExtra("text"),RMessage.class);
//        byte[] data = rMessage.getPhoto();
//        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
//        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outPut);
//        imageView.setImageBitmap(bmp);
    }
}
