package com.example.z1229.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.z1229.activity.PlusItemActivity;
import com.example.z1229.activity.PlusNewActivity;
import com.example.z1229.adapter.PlusAdapter;
import com.example.z1229.bean.CommentBean;
import com.example.z1229.bean.Dynamic;
import com.example.z1229.bean.Message;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.z1229.activity.LoadActivity.SOCKET_BINDER;

public class PlusFragment extends Fragment{
    @BindView(R.id.plus_image)
    RoundedImageView plusImage;
    @BindView(R.id.plus_new)
    ImageButton plusNew;
    @BindView(R.id.plus_tab)
    TabLayout plusTab;
    @BindView(R.id.plus_list)
    RecyclerView plusList;
    @BindView(R.id.plus_refresh)
    FloatingActionButton plusRefresh;

    private Unbinder unbinder;
    private PlusAdapter plusAdapter;
    private Gson gson = new Gson();
    public static final String ACTION_SOCKET = "com.example.z1229.receiver.socketReceiver";
    private LocalBroadcastManager broadcastManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus, container, false);
        unbinder = ButterKnife.bind(this, view);
        initTab();
        initRecycler();

        //注册广播接收器
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SOCKET);
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        plusAdapter.clear();
        loadData(-2);
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

    /**
     * 从服务器查用户的动态信息
     * @param type 广场刷新(-1),广场加载全部(-2),关注刷新(-3),关注全部(-4)
     */
    private void loadData(int type){
        Dynamic dynamic = new Dynamic();
        dynamic.setType("下载");
        dynamic.setReceiverPhone(15083498391L);
        dynamic.setState(type);
        Message message = new Message("Dynamic",gson.toJson(dynamic));
        SOCKET_BINDER.send(gson.toJson(message));
    }

    private void initTab() {
        plusTab.addTab(plusTab.newTab().setText("广  场"));
        plusTab.addTab(plusTab.newTab().setText("关  注"));
        plusTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    plusAdapter.clear();
                    loadData(-2);
                }else {
//                    loadData(-4);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initRecycler() {
        plusAdapter = new PlusAdapter(getActivity());
        plusList.setLayoutManager(new LinearLayoutManager(getActivity()));
        plusList.setAdapter(plusAdapter);
        plusAdapter.setOnItemClickListener(new PlusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CommentBean commentBean=new CommentBean();
                commentBean.setDyId(plusAdapter.getDynamic(position).getDyid());
                commentBean.setType("浏览");
                Message message=new Message("CommentBean",gson.toJson(commentBean));
                SOCKET_BINDER.send(gson.toJson(message));
                Intent intent = new Intent(getActivity(),PlusItemActivity.class);
                String dynamic = gson.toJson(plusAdapter.getDynamic(position));
                intent.putExtra("dynamic",dynamic);
                intent.putExtra("from","item");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @OnClick({R.id.plus_image, R.id.plus_new, R.id.plus_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.plus_image:
                break;
            case R.id.plus_new:
                Intent intent = new Intent(getActivity(),PlusNewActivity.class);
                startActivity(intent);
                break;
            case R.id.plus_refresh:
//                loadData(type);
                break;
        }
    }
}
