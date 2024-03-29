package com.example.z1229.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.z1229.summerclient.R;

import java.util.List;

/**
 * Recycler View Adapter
 * Created by wudeng on 2017/3/9.
 */

public abstract class RecycleViewAdapter<T> extends RecyclerView.Adapter<RViewHolder> {

    private Context mContext;
    private List<T> mList;
    private LayoutInflater mInflater;
    private OnItemLongClickListener mLongClickListener;
    private OnItemClickListener mClickListener;

    public RecycleViewAdapter(Context context, List<T> data){
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int itemLayout) {
        View view = mInflater.inflate(itemLayout, parent, false);
        return new RViewHolder(mContext,view);
    }

    @Override
    public void onBindViewHolder(final RViewHolder holder,int position) {
        View view = holder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null){
                    mClickListener.onItemClick(holder,holder.getAdapterPosition());
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null){
                    mLongClickListener.onItemLongClick(holder,holder.getAdapterPosition());
                }
                return false;
            }
        });
        bindView(holder,position);
    }

    @Override
    public int getItemViewType(int position) {
        return this.setItemLayoutId(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    /**
     * set item layout id
     * @param position item'position in list
     * @return layout id
     */
    public abstract int setItemLayoutId(int position);

    /**
     * bind view by holder
     * @param holder view holder
     * @param position position in data list
     */
    public abstract void bindView(RViewHolder holder, int position);

    public void setItemLongClickListener(OnItemLongClickListener longClickListener){
        this.mLongClickListener = longClickListener;
    }

    public void setItemClickListener(OnItemClickListener clickListener){
        this.mClickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RViewHolder holder,int position);
    }

   /* public interface OnItemChildrenClickListener1{//点击textView阅读详情
        void onChildClick(View view,int position);
    }*/
}
