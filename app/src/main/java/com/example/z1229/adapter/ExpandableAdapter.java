package com.example.z1229.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> groupData = new ArrayList<>();
    private ArrayList<ArrayList<ItemInfo>> childData = new ArrayList<>();
    private Context context;

    public ExpandableAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_eat_list, parent, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.eatListGroup.setText(groupData.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_eat_select, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (ChildViewHolder)convertView.getTag();
        }
        childViewHolder.image.setImageResource(childData.get(groupPosition).get(childPosition).getPhoto());
        childViewHolder.name.setText(childData.get(groupPosition).get(childPosition).getName());
        childViewHolder.cal.setText(childData.get(groupPosition).get(childPosition).getCal()+"");
        childViewHolder.k.setText(childData.get(groupPosition).get(childPosition).getKe()+"克");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        @BindView(R.id.eat_list_group)
        TextView eatListGroup;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.eat_select_item_item)
        ImageView image;
        @BindView(R.id.eat_select_item)
        TextView name;
        @BindView(R.id.eat_select_item_k)
        TextView cal;
        @BindView(R.id.eat_select_item_ke)
        TextView k;
        @BindView(R.id.eat_select_item_image)
        ImageView ok;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setData(ArrayList<String> groupData,ArrayList<ArrayList<ItemInfo>> childData){
        this.groupData = groupData;
        this.childData = childData;
        notifyDataSetChanged();
    }

    public void clear(){
        groupData.clear();
        childData.clear();
        notifyDataSetChanged();
    }
}
