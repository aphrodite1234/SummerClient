package com.example.z1229.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.summerclient.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClockAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private Context context;
    private LinkedList<Bundle> linkedList;

    public ClockAdapter(Context context,LinkedList<Bundle> linkedList){
        this.context = context;
        this.linkedList = linkedList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clock_ac,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.aSwitch = (Switch)convertView.findViewById(R.id.clock_switch);
            viewHolder.remark = (TextView)convertView.findViewById(R.id.clock_remark);
            viewHolder.repeat = (TextView)convertView.findViewById(R.id.clock_repeat);
            viewHolder.time = (TextView)convertView.findViewById(R.id.clock_time);
            viewHolder.delete = (Button)convertView.findViewById(R.id.clock_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        initdata(position,viewHolder);

        return convertView;
    }

    private void initdata(final int position, ViewHolder viewHolder){
        Bundle bundle = linkedList.get(position);
        ArrayList<Integer> arrayList = bundle.getIntegerArrayList("repeat");
        String[] week = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        viewHolder.time.setText(String.format("%02d : %02d",bundle.getInt("h"),bundle.getInt("m")));
        viewHolder.repeat.setText(bundle.getString("repeat"));
        viewHolder.remark.setText(bundle.getString("remark"));
        viewHolder.aSwitch.setOnCheckedChangeListener(this);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(position);
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("确定删除这个提醒？");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.isChecked()){
            Toast.makeText(context,"开关:ON",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"开关:OFF",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getCount() {
        if (linkedList != null){
            return linkedList.size();
        }else {
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

    static class ViewHolder{
        TextView time,remark,repeat;
        Switch aSwitch;
        Button delete;
    }

    public void add(Bundle bundle){
        if(linkedList == null){
            linkedList = new LinkedList<>();
        }
        linkedList.add(bundle);
        notifyDataSetChanged();
    }

    public void update(int position,Bundle bundle){
        if(linkedList == null){
            linkedList = new LinkedList<>();
        }
        linkedList.set(position,bundle);
        notifyDataSetChanged();
    }

    public void delete(int position){
        if(linkedList != null){
            linkedList.remove(position);
        }
        notifyDataSetChanged();
    }

    public Bundle getdata(int position){
        return linkedList.get(position);
    }
}
