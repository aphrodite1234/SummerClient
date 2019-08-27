package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.assionhonty.lib.assninegridview.ImageInfo;
import com.bumptech.glide.Glide;
import com.example.z1229.adapter.AssPreviewAdapter;
import com.example.z1229.adapter.PlusItemAdapter;
import com.example.z1229.adapter.PlusItemZanAdapter;
import com.example.z1229.base.CommentDialog;
import com.example.z1229.bean.CommentBean;
import com.example.z1229.bean.Dynamic;
import com.example.z1229.bean.Message;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.z1229.activity.LoadActivity.SOCKET_BINDER;

public class PlusItemActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.plus_item_image)
    RoundedImageView userInfo;
    @BindView(R.id.plus_item_name)
    TextView userName;
    @BindView(R.id.plus_item_category)
    TextView category;
    @BindView(R.id.plus_item_content)
    TextView content;
    @BindView(R.id.plus_item_pictures)
    AssNineGridView pictures;
    @BindView(R.id.plus_item_time)
    TextView time;
    @BindView(R.id.plus_item_comment_count)
    TextView commentCount;
    @BindView(R.id.plus_item_comment_r)
    RelativeLayout comment;
    @BindView(R.id.plus_item_zan)
    ImageView zanImage;
    @BindView(R.id.plus_item_zan_count)
    TextView zanCount;
    @BindView(R.id.plus_item_zan_r)
    RelativeLayout zan;
    @BindView(R.id.plus_item_tab)
    TabLayout plusItemTab;
    @BindView(R.id.plus_item_list)
    RecyclerView plusItemList;
    @BindView(R.id.plus_refresh)
    FloatingActionButton plusRefresh;
    private Gson gson = new Gson();
    private Dynamic dynamic;
    private PlusItemAdapter plusItemAdapter;
    private PlusItemZanAdapter plusItemZanAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String type="下载评论";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_item);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        dynamic = gson.fromJson(intent.getStringExtra("dynamic"),Dynamic.class);
        String from=intent.getStringExtra("from");
        if(from.equals("comment")){
            showCommentDialog("发表评论",new CommentBean());
        }
        initViewData();
        initTab();
        initList();
        showList(0);
        loadComment(type);
    }

    @Override
    protected void doAction(String content) {
        Message message = gson.fromJson(content, Message.class);
        if (message.getType().equals("CommentBean")) {
            CommentBean comment = gson.fromJson(message.getContent(), CommentBean.class);
            String type=comment.getType();
            int id=comment.getId();
            if(type.equals("下载评论")){
                plusItemAdapter.addData(comment);
                commentCount.setText(String.valueOf(comment.getDyId()));
                zanCount.setText(String.valueOf(comment.getState()));
            }else if(type.equals("下载赞")){
                plusItemZanAdapter.addData(comment);
            }else if(type.equals("上传赞")&&id==1){
                int index=plusItemAdapter.getIndex();
                CommentBean commentBean=plusItemAdapter.getData(index);
                commentBean.setZanBool(1);
                commentBean.setZanCount(commentBean.getZanCount()+1);
                plusItemAdapter.updateData(index,commentBean);
            }else if (type.equals("取消赞")&&id==1){
                int index=plusItemAdapter.getIndex();
                CommentBean commentBean=plusItemAdapter.getData(index);
                commentBean.setZanBool(0);
                commentBean.setZanCount(commentBean.getZanCount()-1);
                plusItemAdapter.updateData(index,commentBean);
            }else if(type.equals("动态赞")&&id==1){
                dynamic.setZan_bool(1);
                dynamic.setZan_count(dynamic.getZan_count()+1);
                updateZan();
            }else if(type.equals("取消动态赞")&&id==1){
                dynamic.setZan_bool(0);
                dynamic.setZan_count(dynamic.getZan_count()-1);
                updateZan();
            }else if(type.equals("上传评论")&&id==1){
                loadComment(this.type);
            }
        }
    }

    private void initTab(){
        plusItemTab.addTab(plusItemTab.newTab().setText("评论"));
        plusItemTab.addTab(plusItemTab.newTab().setText("点赞"));
        plusItemTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int po=tab.getPosition();
                showList(po);
                if(po==0){
                    type="下载评论";
                    loadComment(type);
                }else {
                    type="下载赞";
                    loadComment(type);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViewData(){
        Glide.with(this).load(dynamic.getUrl().get(0)).centerCrop().into(userInfo);
        userName.setText(String.valueOf(dynamic.getSenderPhone()));
        category.setText(dynamic.getB_type());
        content.setText(dynamic.getContent());
        time.setText(dynamic.getDytime());
        commentCount.setText(String.valueOf(dynamic.getComment_count()));
        updateZan();
        List<ImageInfo> imageInfos = getImageInfos(dynamic.getUrl());
        pictures.setAdapter(new AssPreviewAdapter(this,imageInfos));
    }

    private void updateZan(){
        zanCount.setText(String.valueOf(dynamic.getZan_count()));
        if(dynamic.getZan_bool()==1){
            Glide.with(this).load(R.drawable.ic_zan_pressed).into(zanImage);
            zanCount.setTextColor(getResources().getColor(R.color.icon_more));
        }else if(dynamic.getZan_bool()==0){
            Glide.with(this).load(R.drawable.ic_zan).into(zanImage);
            zanCount.setTextColor(getResources().getColor(R.color.gray_font));
        }
    }

    private void initList(){
        layoutManager=new LinearLayoutManager(this);
        plusItemAdapter=new PlusItemAdapter(this);
        plusItemZanAdapter=new PlusItemZanAdapter(this);
        plusItemList.setLayoutManager(layoutManager);
        plusItemAdapter.setOnItemClickListener(new PlusItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CommentBean commentBean=plusItemAdapter.getData(position);
                showCommentDialog("回复"+commentBean.getSenderName(),plusItemAdapter.getData(position));
            }
        });
        plusItemZanAdapter.setOnItemClickListener(new PlusItemZanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(PlusItemActivity.this,UserActivity.class);
                intent.putExtra("userPhone",plusItemZanAdapter.getData(position).getSenderPhone());
                startActivity(intent);
            }
        });
    }

    private void showList(int po){
        if(po==0){
            plusItemList.setAdapter(plusItemAdapter);
        }else {
            plusItemList.setAdapter(plusItemZanAdapter);
        }
    }

    /**
     * @param type 下载评论/下载赞
     */
    private void loadComment(String type){
        plusItemZanAdapter.clear();
        plusItemAdapter.clear();
        CommentBean comment=new CommentBean();
        comment.setSenderPhone(15083498391L);
        comment.setDyId(dynamic.getDyid());
        comment.setType(type);
        Message message=new Message("CommentBean",gson.toJson(comment));
        SOCKET_BINDER.send(gson.toJson(message));
    }

    private void zan(){
        CommentBean commentBean=new CommentBean();
        if(dynamic.getZan_bool()==0){
            commentBean.setType("动态赞");
            commentBean.setId(-1);
        }else {
            commentBean.setType("取消动态赞");
        }
        commentBean.setSenderPhone(15083498391L);
        commentBean.setDyId(dynamic.getDyid());
        commentBean.setDateTime(new Date());
        Message message=new Message("CommentBean",gson.toJson(commentBean));
        SOCKET_BINDER.send(gson.toJson(message));
    }

    /**
     * @param content 评论的内容
     * @param com 获取接收者
     */
    private void upComment(String content,CommentBean com){
        CommentBean commentBean=new CommentBean();
        commentBean.setType("上传评论");
        commentBean.setDyId(dynamic.getDyid());
        commentBean.setSenderPhone(15083498391L);
        commentBean.setSenderName("张三");
        commentBean.setReceiverPhone(com.getSenderPhone());
        commentBean.setReceiverName(com.getSenderName());
        commentBean.setContent(content);
        commentBean.setDateTime(new Date());
        Message message=new Message("CommentBean",gson.toJson(commentBean));
        SOCKET_BINDER.send(gson.toJson(message));
    }

    /**
     * @param url 图片的地址
     * @return
     */
    private List<ImageInfo> getImageInfos(ArrayList<String> url) {
        List<ImageInfo> imageInfos = new ArrayList<>();
        for (int i=1;i<url.size();i++){
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setBigImageUrl(url.get(i));
            imageInfo.setThumbnailUrl(url.get(i));
            imageInfos.add(imageInfo);
        }
        return imageInfos;
    }

    /**
     * @param string 输入提示
     * @param commentBean 如从adapter获取，为回复别人评论
     */
    private void showCommentDialog(final String string, final CommentBean commentBean) {
        new CommentDialog(string, new CommentDialog.SendListener() {
            @Override
            public void sendComment(String inputText) {
                upComment(inputText,commentBean);
            }
        }).show(getSupportFragmentManager(), "comment");
    }

    @OnClick({R.id.btn_back, R.id.plus_item_image, R.id.plus_item_comment_r, R.id.plus_item_zan_r, R.id.plus_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.plus_item_image:
                Intent intent=new Intent(this,UserActivity.class);
                intent.putExtra("userPhone",dynamic.getSenderPhone());
                startActivity(intent);
                break;
            case R.id.plus_item_comment_r:
                showCommentDialog("发表评论",new CommentBean());
                break;
            case R.id.plus_item_zan_r:
                zan();
                break;
            case R.id.plus_refresh:
                loadComment(type);
                break;
        }
    }
}
