package com.example.z1229.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SportAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemInfo> data = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();

    public SportAdapter() {
    }

    public SportAdapter(Context context, ArrayList<ItemInfo> arrayList) {
        this.context = context;
        data = arrayList;
    }
    public SportAdapter(Context context, ArrayList<ItemInfo> arrayList,ArrayList<String> time) {
        this.context = context;
        data = arrayList;
        this.time = time;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sport_select, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ItemInfo itemInfo = data.get(position);
        viewHolder.sportSelectItemImg.setImageResource(itemInfo.getPhoto());
        viewHolder.sportSelectItemName.setText(itemInfo.getName());
        viewHolder.sportSelectItemK.setText(itemInfo.getCal() + "");
        if(itemInfo.getKe()>0 && time.size()>0){
            viewHolder.sportSelectItemLong.setText(itemInfo.getKe()+"min");
            viewHolder.sportSelectItemBegin.setText(time.get(position));
        }else {
            viewHolder.sportSelectItemTime.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.sport_select_item_img)
        ImageView sportSelectItemImg;
        @BindView(R.id.sport_select_item_name)
        TextView sportSelectItemName;
        @BindView(R.id.sport_select_item_k)
        TextView sportSelectItemK;
        @BindView(R.id.sport_select_item_long)
        TextView sportSelectItemLong;
        @BindView(R.id.sport_select_item_begin)
        TextView sportSelectItemBegin;
        @BindView(R.id.sport_select_item_time)
        LinearLayout sportSelectItemTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
