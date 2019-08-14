package com.example.z1229.adapter;

import android.content.SyncAdapterType;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EatAdapter extends RecyclerView.Adapter<EatAdapter.VH> {

    private OnItemClickListener clickListener;
    private ArrayList<ItemInfo> data;
    private String name;

    public EatAdapter(ArrayList<ItemInfo> data){
        this.data = data;
    }

    public EatAdapter(){

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_eat_select, viewGroup, false);
        return new VH(v,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        if(null != data){
            ItemInfo itemInfo = data.get(i);
            String itemname = itemInfo.getName();
            vh.eat.setImageResource(itemInfo.getPhoto());
            vh.cal.setText(String.valueOf(itemInfo.getCal()));
            if(itemInfo.getKe() < 0){
                vh.ke.setVisibility(View.INVISIBLE);
                vh.ok.setVisibility(View.INVISIBLE);
                int star = itemname.indexOf(name);
                SpannableString span = new SpannableString(itemname);
                span.setSpan(new ForegroundColorSpan(Color.RED),star,star+name.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                vh.name.setText(span);
            }else if(itemInfo.getKe() > 0){
                vh.name.setText(itemname);
                vh.ke.setVisibility(View.VISIBLE);
                vh.ok.setVisibility(View.VISIBLE);
                if(itemInfo.getKe() != 0){
                    vh.ke.setText(itemInfo.getKe()+"克");
                }
            }else {
                vh.name.setText(itemname);
                vh.ke.setVisibility(View.INVISIBLE);
                vh.ok.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (data != null){
            return data.size();
        }else {
            return 0;
        }
    }


    public static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.eat_select_item_item)
        ImageView eat;
        @BindView(R.id.eat_select_item)
        TextView name;
        @BindView(R.id.eat_select_item_k)
        TextView cal;
        @BindView(R.id.eat_select_item_ke)
        TextView ke;
        @BindView(R.id.eat_select_item_image)
        ImageView ok;
        public VH(@NonNull View itemView,final OnItemClickListener onClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
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

    public void updateItem(int position,ItemInfo itemInfo){
        data.set(position,itemInfo);
        notifyDataSetChanged();
    }

    public void addData(ItemInfo itemInfo,String string){
        data.add(itemInfo);
        name = string;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<ItemInfo> arrayList){
        data = arrayList;
        notifyDataSetChanged();
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
