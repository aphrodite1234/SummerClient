package com.example.z1229.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.z1229.activity.UserActivity;
import com.example.z1229.base.MyLinkMovementMethod;
import com.example.z1229.base.MySpannableTextView;
import com.example.z1229.bean.CommentBean;
import com.example.z1229.bean.Message;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.z1229.activity.LoadActivity.SOCKET_BINDER;

public class PlusItemAdapter extends RecyclerView.Adapter<PlusItemAdapter.VH> {

    private Context context;
    private ArrayList<CommentBean> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private int index;

    public PlusItemAdapter(){}
    public PlusItemAdapter(Context context){
        this.context = context;
    }
    public PlusItemAdapter(Context context,ArrayList<CommentBean> data){
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<CommentBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void updateData(int index,CommentBean commentBean){
        data.set(index,commentBean);
        notifyDataSetChanged();
    }

    public CommentBean getData(int position) {
        return data.get(position);
    }
    public void addData(CommentBean commentBean){
        data.add(commentBean);
        notifyDataSetChanged();
    }
    public void addData(int index,CommentBean commentBean){
        data.add(index,commentBean);
        notifyDataSetChanged();
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_plus_item_com, viewGroup, false);
        return new VH(view,onItemClickListener);
    }



    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, final int i) {
        SpannableStringBuilder builder=new SpannableStringBuilder();
        CommentBean commentBean = data.get(i);
        final String senderName=commentBean.getSenderName(),receiverName=commentBean.getReceiverName();
        SpannableString sender=new SpannableString(senderName);
        sender.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.icon_more)),
                0,senderName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sender.setSpan(new ClickableSpan(){
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent=new Intent(context,UserActivity.class);
                intent.putExtra("userPhone",data.get(i).getSenderPhone());
                context.startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        },0,senderName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if(commentBean.getReceiverPhone()>0){
            SpannableString receiver=new SpannableString(receiverName);
            receiver.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.icon_more)),
                    0,senderName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            receiver.setSpan(new ClickableSpan(){
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent=new Intent(context,UserActivity.class);
                    intent.putExtra("userPhone",data.get(i).getReceiverPhone());
                    context.startActivity(intent);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            },0,senderName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.append(sender).append(" 回复 ").append(receiver).append(":").append(commentBean.getContent());
        }else {
            builder.append(sender).append(":").append(commentBean.getContent());
        }
        MyLinkMovementMethod myLinkMovementMethod=new MyLinkMovementMethod();
        viewHolder.comment.setText(builder);
        viewHolder.comment.setLinkTouchMovementMethod(myLinkMovementMethod);
        viewHolder.comment.setMovementMethod(myLinkMovementMethod);

        viewHolder.zanCount.setText(String.valueOf(commentBean.getZanCount()));
        if(commentBean.getZanBool() == 1){
            Glide.with(context).load(R.drawable.ic_zan_pressed).into(viewHolder.zanImage);
            viewHolder.zanCount.setTextColor(context.getResources().getColor(R.color.icon_more));
        }else if(commentBean.getZanBool()==0){
            Glide.with(context).load(R.drawable.ic_zan).into(viewHolder.zanImage);
            viewHolder.zanCount.setTextColor(context.getResources().getColor(R.color.gray_font));
        }
        viewHolder.zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=i;
                Gson gson = new Gson();
                CommentBean comment=new CommentBean();
                if(data.get(i).getZanBool()==0){
                    comment.setType("上传赞");
                    comment.setDyId(-1);
                }else {
                    comment.setType("取消赞");
                }
                comment.setSenderPhone(15083498391L);
                comment.setId(data.get(i).getId());
                comment.setDateTime(new Date());
                Message message=new Message("CommentBean",gson.toJson(comment));
                SOCKET_BINDER.send(gson.toJson(message));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getIndex() {
        return index;
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.plus_item_com_content)
        MySpannableTextView comment;
        @BindView(R.id.plus_item_com_r)
        LinearLayout zan;
        @BindView(R.id.plus_item_com_zan)
        ImageView zanImage;
        @BindView(R.id.plus_item_com_zan_count)
        TextView zanCount;

        VH(View view, final OnItemClickListener onClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        //确保position值有效
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
