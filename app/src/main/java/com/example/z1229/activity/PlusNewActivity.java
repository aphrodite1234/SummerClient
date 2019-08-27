package com.example.z1229.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.z1229.adapter.NewGridAdapter;
import com.example.z1229.bean.Dynamic;
import com.example.z1229.bean.Message;
import com.example.z1229.summerclient.R;
import com.example.z1229.utils.PictureUtils;
import com.google.gson.Gson;
import com.zzti.fengyongge.imagepicker.PhotoPreviewActivity;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.z1229.activity.LoadActivity.SOCKET_BINDER;

public class PlusNewActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.plus_new_send)
    ImageButton plusNewSend;
    @BindView(R.id.plus_new_text)
    EditText plusNewText;
    @BindView(R.id.plus_new_img)
    GridView plusNewImg;
    @BindView(R.id.plus_new_at)
    RelativeLayout plusNewAt;
    private NewGridAdapter gridAdapter;
    private int limit = 9;
    private AlertDialog alertDialog;
    private Gson gson = new Gson();
    private Dynamic dynamic = new Dynamic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_new);
        ButterKnife.bind(this);
        initGrid();
    }

    @Override
    protected void doAction(String content) {
        Message message = gson.fromJson(content,Message.class);
        if(message.getType().equals("Dynamic")){
            Dynamic dy=gson.fromJson(message.getContent(),Dynamic.class);
            if(dy.getType().equals("true")){
                alertDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }else if(dy.getType().equals("false")){
                alertDialog.dismiss();
                Toast.makeText(this,"失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initGrid() {
        gridAdapter = new NewGridAdapter(this);
        plusNewImg.setAdapter(gridAdapter);
        plusNewImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int l=gridAdapter.getData().size();
                limit=l>=9 ? 0:9-l;
                if (limit>0 && gridAdapter.getCount() == position+1) {
                    Intent intent = new Intent(PlusNewActivity.this, PhotoSelectorActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("limit", limit);//number是选择图片的数量
                    startActivityForResult(intent, 0);
                } else {
                    List<PhotoModel> single_photos = new ArrayList<>();
                    //PhotoModel 开发者将自己本地bean的list封装成PhotoModel的list，PhotoModel属性源码可查看
                    for (String path:gridAdapter.getData()){
                        single_photos.add(new PhotoModel(path));
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos", (Serializable) single_photos);
                    bundle.putInt("position", position);//position预览图片地址
                    bundle.putBoolean("isSave", false);//isSave表示是否可以保存预览图片，建议只有预览网络图片时设置true
                    CommonUtils.launchActivity(PlusNewActivity.this, PhotoPreviewActivity.class, bundle);
                }
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.plus_new_send, R.id.plus_new_at})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.plus_new_send:
                sendDy();
                break;
            case R.id.plus_new_at:
                break;
        }
    }

    private void sendDy(){
        dynamic.setType("上传");
        dynamic.setSenderPhone(15083498391L);
        dynamic.setContent(plusNewText.getText().toString().trim());
        dynamic.setB_type("未知");
        ArrayList<byte[]> picture = new ArrayList<>();
        for (String p:gridAdapter.getData()){
            picture.add(PictureUtils.file2byte(p));
        }
        dynamic.setPicture(picture);
        dynamic.setDytime(new Date());
        Message message = new Message("Dynamic",gson.toJson(dynamic));
        boolean b = SOCKET_BINDER.send(gson.toJson(message));
        sendDialog();
        if(!b){
            alertDialog.dismiss();
            Toast.makeText(this,"发送失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDialog(){
        LayoutInflater inflater = PlusNewActivity.this.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_progress,null,false);
        AlertDialog.Builder builder = new AlertDialog.Builder(PlusNewActivity.this);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        WindowManager.LayoutParams  lp= Objects.requireNonNull(alertDialog.getWindow()).getAttributes();
        lp.width = 600;
        lp.height = 600;
        lp.alpha = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.getWindow().setContentView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 0:
                    if (data != null) {
                        ArrayList<String> data1 = (ArrayList<String>) data.getExtras().getSerializable("photos");
                        gridAdapter.addData(data1);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
