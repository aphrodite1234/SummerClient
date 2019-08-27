package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.assionhonty.lib.assninegridview.ImageInfo;
import com.assionhonty.lib.assninegridview.assImgPreview.AssImgPreviewActivity;
import com.bumptech.glide.Glide;
import com.example.z1229.adapter.PlusAdapter;
import com.example.z1229.bean.CommentBean;
import com.example.z1229.bean.Dynamic;
import com.example.z1229.bean.Message;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.z1229.activity.LoadActivity.SOCKET_BINDER;

public class UserActivity extends BaseActivity {

    @BindView(R.id.user_back)
    ImageButton back;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_list)
    RecyclerView userList;
    @BindView(R.id.user_refresh)
    FloatingActionButton refresh;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_cate)
    TextView userCate;
    private PlusAdapter dyAdapter;
    private Gson gson = new Gson();
    private Long userPhone;
    private boolean count=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userPhone=getIntent().getLongExtra("userPhone",0);
        ButterKnife.bind(this);
        initList();
        loadData(-6);
    }

    @Override
    protected void doAction(String content) {
        Message message = gson.fromJson(content, Message.class);
        if (message.getType().equals("Dynamic")) {
            Dynamic dynamic = gson.fromJson(message.getContent(), Dynamic.class);
            if (dynamic.getType().equals("下载")) {
                dyAdapter.addData(dynamic);
                if(count){
                    initViewData(dynamic);
                    count=false;
                }
            }
        }
        if(message.getType().equals("CommentBean")){
            CommentBean comment=gson.fromJson(message.getContent(),CommentBean.class);
            String type=comment.getType();
            int id=comment.getId();
            int index=dyAdapter.getIndex();
            Dynamic dynamic=dyAdapter.getDynamic(index);
            if(type.equals("上传赞")&&id==1){
                dynamic.setZan_bool(1);
                dynamic.setZan_count(dynamic.getZan_count()+1);
                dyAdapter.updateData(index,dynamic);
            }else if(type.equals("取消赞")&&id==1){
                dynamic.setZan_bool(0);
                dynamic.setZan_count(dynamic.getZan_count()-1);
                dyAdapter.updateData(index,dynamic);
            }
        }
    }

    private void initViewData(Dynamic dynamic) {
        Glide.with(this).load(dynamic.getUrl().get(0)).centerCrop().into(userImage);
        userCate.setText(dynamic.getB_type());
        userName.setText(dynamic.getSenderName());
    }

    private void initList() {
        dyAdapter = new PlusAdapter(this);
        userList.setLayoutManager(new LinearLayoutManager(this));
        userList.setAdapter(dyAdapter);
        dyAdapter.setOnItemClickListener(new PlusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CommentBean commentBean=new CommentBean();
                commentBean.setDyId(dyAdapter.getDynamic(position).getDyid());
                Message message=new Message("CommentBean",gson.toJson(commentBean));
                SOCKET_BINDER.send(gson.toJson(message));
                Intent intent = new Intent(UserActivity.this, PlusItemActivity.class);
                String con = gson.toJson(dyAdapter.getDynamic(position));
                intent.putExtra("dynamic", con);
                intent.putExtra("from", "item");
                startActivity(intent);
            }
        });
    }

    /**
     * 从服务器查用户的动态信息
     *
     * @param type 个人刷新(-5),个人全部(-6)
     */
    private void loadData(int type) {
        Dynamic dynamic = new Dynamic();
        dynamic.setType("下载");
        dynamic.setSenderPhone(userPhone);
        dynamic.setReceiverPhone(15083498391L);
        dynamic.setState(type);
        Message message = new Message("Dynamic", gson.toJson(dynamic));
        SOCKET_BINDER.send(gson.toJson(message));
    }


    private void previewImage(String url) {
        List<ImageInfo> imageInfo = new ArrayList<>();
        ImageInfo image = new ImageInfo();
        image.setBigImageUrl(url);
        image.setThumbnailUrl(url);
        imageInfo.add(image);
        Intent intent = new Intent(this, AssImgPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("imageInfo", (Serializable) imageInfo);
        bundle.putInt("currentIndex", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.user_back, R.id.user_image, R.id.user_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_back:
                finish();
                break;
            case R.id.user_image:
//                previewImage(dyAdapter.getDynamic(0).getUrl().get(0));
                break;
            case R.id.user_refresh:
                break;
        }
    }
}
