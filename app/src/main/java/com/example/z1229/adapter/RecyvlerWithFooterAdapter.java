package com.example.z1229.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.z1229.summerclient.R;

import java.util.List;

/**
 * Created by MaiBenBen on 2019/8/25.
 */

public abstract class RecyvlerWithFooterAdapter<T> extends RecyclerView.Adapter<RViewHolder> {

    private Context mContext;
    private List<T> mList;
    private LayoutInflater mInflater;
    private int normalType = 0;
    private int footType = 1;
    private RecycleViewAdapter.OnItemLongClickListener mLongClickListener;
    private RecycleViewAdapter.OnItemClickListener mClickListener;

    public RecyvlerWithFooterAdapter(Context context, List<T> data){
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
        if(position==mList.size()-1)
            return R.layout.recyclerview_footer;
        else
            return this.setItemLayoutId(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size()+1;
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

    public void setItemLongClickListener(RecycleViewAdapter.OnItemLongClickListener longClickListener){
        this.mLongClickListener = longClickListener;
    }

    public void setItemClickListener(RecycleViewAdapter.OnItemClickListener clickListener){
        this.mClickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RViewHolder holder,int position);
    }

}
