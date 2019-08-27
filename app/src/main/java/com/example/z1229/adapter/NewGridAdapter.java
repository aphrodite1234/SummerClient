package com.example.z1229.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.z1229.summerclient.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGridAdapter extends BaseAdapter {
    private Context context;
    private int maxImg = 9;
    private ArrayList<String> data = new ArrayList<>();

    public NewGridAdapter(){}
    public NewGridAdapter(Context context){this.context = context;}

    @Override
    public int getCount() {
        int count = data == null ? 1 : data.size() + 1;
        if (count > maxImg) {
            return data.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plus_new_grid, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(data!=null&&position<data.size()){
            final File file = new File(data.get(position));
            Glide.with(context).load(file).centerCrop().into(viewHolder.image);
            viewHolder.btn.setVisibility(View.VISIBLE);
            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        file.delete();
                    }
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });
        }else {
            Glide.with(context).load(R.drawable.ic_plus_add).into(viewHolder.image);
            viewHolder.btn.setVisibility(View.GONE);
            viewHolder.image.setScaleType(PhotoView.ScaleType.FIT_XY);
        }

       return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.plus_new_item_image)
        ImageView image;
        @BindView(R.id.plus_new_item_btn)
        Button btn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<String> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public ArrayList<String> getData(){
        return data;
    }
}
