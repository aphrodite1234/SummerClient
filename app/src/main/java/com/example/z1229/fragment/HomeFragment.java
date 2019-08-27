package com.example.z1229.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.z1229.activity.BloodAddActivity;
import com.example.z1229.activity.LoadActivity;
import com.example.z1229.activity.PlusItemActivity;
import com.example.z1229.adapter.PlusAdapter;
import com.example.z1229.bean.CommentBean;
import com.example.z1229.bean.Dynamic;
import com.example.z1229.bean.Message;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private PlusAdapter plusAdapter;
    public static final String ACTION_SOCKET = "com.example.z1229.receiver.socketReceiver";
    private LocalBroadcastManager broadcastManager;
    private Gson gson=new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initRecycler();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        plusAdapter.clear();
        loadData();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }
    public void initView(){
        view.findViewById(R.id.home_add_record).setOnClickListener(this);
        view.findViewById(R.id.home_GI_value).setOnClickListener(this);
        view.findViewById(R.id.home_science_diet).setOnClickListener(this);
        view.findViewById(R.id.home_wiki).setOnClickListener(this);
        mRecyclerView=view.findViewById(R.id.home_social_recycle);
        layoutManager=new LinearLayoutManager(getActivity());
        plusAdapter=new PlusAdapter(getActivity(),true);
        //注册广播接收器
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SOCKET);
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initRecycler(){
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(plusAdapter);
        plusAdapter.setOnItemClickListener(new PlusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CommentBean commentBean=new CommentBean();
                commentBean.setDyId(plusAdapter.getDynamic(position).getDyid());
                commentBean.setType("浏览");
                Message message=new Message("CommentBean",gson.toJson(commentBean));
//                SOCKET_BINDER.send(gson.toJson(message));
                Intent intent = new Intent(getActivity(),PlusItemActivity.class);
                String dynamic = gson.toJson(plusAdapter.getDynamic(position));
                intent.putExtra("dynamic",dynamic);
                intent.putExtra("from","item");
                startActivity(intent);
            }
        });
    }

    private void loadData(){
        Dynamic dynamic = new Dynamic();
        dynamic.setType("下载");
        dynamic.setReceiverPhone(15083498391L);
        dynamic.setState(-7);
        Message message = new Message("Dynamic",gson.toJson(dynamic));
        LoadActivity.SOCKET_BINDER.send(gson.toJson(message));
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_add_record:
                startActivity(new Intent(getActivity(),BloodAddActivity.class));
                break;
            case R.id.home_GI_value:
                Toast.makeText(getActivity(),"升糖指数",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_science_diet:
                Toast.makeText(getActivity(),"科学饮食",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_wiki:
                Toast.makeText(getActivity(),"知识百科",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 接受服务器发来的数据
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_SOCKET)) {
                String content = intent.getStringExtra("text");
                Message message = gson.fromJson(content,Message.class);
                if(message.getType().equals("Dynamic")){
                    Dynamic dynamic = gson.fromJson(message.getContent(),Dynamic.class);
                    if(dynamic.getType().equals("下载")){
                        plusAdapter.addData(dynamic);
                    }
                }
                if(message.getType().equals("CommentBean")){
                    CommentBean comment=gson.fromJson(message.getContent(),CommentBean.class);
                    String type=comment.getType();
                    int id=comment.getId();
                    int index=plusAdapter.getIndex();
                    Dynamic dynamic=plusAdapter.getDynamic(index);
                    if(type.equals("上传赞")&&id==1){
                        dynamic.setZan_bool(1);
                        dynamic.setZan_count(dynamic.getZan_count()+1);
                    }else if(type.equals("取消赞")&&id==1){
                        dynamic.setZan_bool(0);
                        dynamic.setZan_count(dynamic.getZan_count()-1);
                    }
                    plusAdapter.updateData(index,dynamic);
                }
            }
        }
    };
}
