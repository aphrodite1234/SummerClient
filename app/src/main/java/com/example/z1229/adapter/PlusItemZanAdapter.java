package com.example.z1229.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.z1229.bean.CommentBean;
import com.example.z1229.summerclient.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlusItemZanAdapter extends RecyclerView.Adapter<PlusItemZanAdapter.VH> {

    private Context context;
    private ArrayList<CommentBean> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public PlusItemZanAdapter(){}
    public PlusItemZanAdapter(Context context){
        this.context = context;
    }
    public PlusItemZanAdapter(Context context,ArrayList<CommentBean> data){
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<CommentBean> data) {
        this.data = data;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_plus_item, viewGroup, false);
        return new VH(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, final int i) {
        CommentBean commentBean = data.get(i);
        Glide.with(context).load(commentBean.getPhoto()).centerCrop().into(viewHolder.photo);
        viewHolder.name.setText(commentBean.getSenderName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.plus_item_zan_image)
        ImageView photo;
        @BindView(R.id.plus_item_zan_name)
        TextView name;

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
