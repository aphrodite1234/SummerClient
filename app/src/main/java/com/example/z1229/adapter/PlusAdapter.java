package com.example.z1229.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assionhonty.lib.assninegridview.AssNineGridView;
import com.assionhonty.lib.assninegridview.AssNineGridViewClickAdapter;
import com.assionhonty.lib.assninegridview.ImageInfo;
import com.bumptech.glide.Glide;
import com.example.z1229.activity.PlusItemActivity;
import com.example.z1229.activity.UserActivity;
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

import static com.example.z1229.activity.LoadActivity.SOCKET_BINDER;

public class PlusAdapter extends RecyclerView.Adapter<PlusAdapter.VH> {

    public Context context;
    private OnItemClickListener clickListener;
    private ArrayList<Dynamic> mData = new ArrayList<>();
    private int index;
    private boolean home=false;

    public PlusAdapter(){}
    public PlusAdapter(Context context){
        this.context = context;
    }
    public PlusAdapter(Context context,boolean home){
        this.context = context;
        this.home=home;
    }
    public PlusAdapter(Context context,ArrayList<Dynamic> mData){
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_plus_recycler, viewGroup, false);
        return new VH(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, final int i) {
        Dynamic dynamic = mData.get(i);
        Glide.with(context).load(dynamic.getUrl().get(0)).centerCrop().into(viewHolder.itemPlusImage);
        viewHolder.itemPlusName.setText(String.valueOf(dynamic.getSenderName()));
        viewHolder.itemPlusCategory.setText(dynamic.getB_type());
        viewHolder.itemPlusContent.setText(dynamic.getContent());
        viewHolder.plusCommentCount.setText(String.valueOf(dynamic.getComment_count()));
        viewHolder.plusZanCount.setText(String.valueOf(dynamic.getZan_count()));
        if(home){
            viewHolder.itemPlusTime.setText("浏览"+String.valueOf(dynamic.getBrocount())+"次");
        }else {
            viewHolder.itemPlusTime.setText(dynamic.getDytime());
        }
        if(dynamic.getZan_bool()==1){
            Glide.with(context).load(R.drawable.ic_zan_pressed).into(viewHolder.plusZanBtn);
            viewHolder.plusZanCount.setTextColor(context.getResources().getColor(R.color.icon_more));
        }else if(dynamic.getZan_bool()==0){
            Glide.with(context).load(R.drawable.ic_zan).into(viewHolder.plusZanBtn);
            viewHolder.plusZanCount.setTextColor(context.getResources().getColor(R.color.gray_font));
        }
        viewHolder.comment_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(context,PlusItemActivity.class);
                intent.putExtra("from","comment");
                String con = gson.toJson(mData.get(i));
                intent.putExtra("dynamic",con);
                context.startActivity(intent);
            }
        });
        viewHolder.zan_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=i;
                Gson gson = new Gson();
                CommentBean comment=new CommentBean();
                if(mData.get(i).getZan_bool()==0){
                    comment.setType("上传赞");
                    comment.setId(-1);
                }else {
                    comment.setType("取消赞");
                }
                comment.setSenderPhone(15083498391L);
                comment.setDyId(mData.get(i).getDyid());
                comment.setDateTime(new Date());
                Message message=new Message("CommentBean",gson.toJson(comment));
                SOCKET_BINDER.send(gson.toJson(message));
                CommentBean commentBean=new CommentBean();
                commentBean.setDyId(mData.get(i).getDyid());
                commentBean.setType("浏览");
                Message message1=new Message("CommentBean",gson.toJson(commentBean));
                SOCKET_BINDER.send(gson.toJson(message1));
            }
        });
        viewHolder.itemPlusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserActivity.class);
                intent.putExtra("userPhone",mData.get(i).getSenderPhone());
                context.startActivity(intent);
            }
        });
        ArrayList<String> url = dynamic.getUrl();
        List<ImageInfo> imageInfos = getImageInfos(url);
        viewHolder.assNineGridView.setAdapter(new AssNineGridViewClickAdapter(context,imageInfos));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }
    public void addData(Dynamic dynamic){
        mData.add(dynamic);
        notifyDataSetChanged();
    }
    public void addData(int index,Dynamic dynamic){
        mData.add(index,dynamic);
        notifyDataSetChanged();
    }
    public void updateData(int index,Dynamic dynamic){
        mData.set(index,dynamic);
        notifyDataSetChanged();
    }
    public Dynamic getDynamic(int position){
        return mData.get(position);
    }

    public int getIndex() {
        return index;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_plus_image)
        RoundedImageView itemPlusImage;
        @BindView(R.id.item_plus_name)
        TextView itemPlusName;
        @BindView(R.id.item_plus_category)
        TextView itemPlusCategory;
        @BindView(R.id.item_plus_content)
        TextView itemPlusContent;
        @BindView(R.id.item_plus_time)
        TextView itemPlusTime;
        @BindView(R.id.plus_comment_btn)
        ImageView plusCommentBtn;
        @BindView(R.id.plus_comment_count)
        TextView plusCommentCount;
        @BindView(R.id.plus_zan_btn)
        ImageView plusZanBtn;
        @BindView(R.id.plus_zan_count)
        TextView plusZanCount;
        @BindView(R.id.plus_comment_r)
        RelativeLayout comment_r;
        @BindView(R.id.plus_zan_r)
        RelativeLayout zan_r;
        @BindView(R.id.item_plus_picture1)
        AssNineGridView assNineGridView;

        VH(View view, final OnItemClickListener onClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        //确保position值有效
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onItemClick(view, position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

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
}
