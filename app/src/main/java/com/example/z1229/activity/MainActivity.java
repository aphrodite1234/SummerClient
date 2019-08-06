package com.example.z1229.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.z1229.base.Tab;
import com.example.z1229.fragment.HomeFragment;
import com.example.z1229.fragment.MoreFragment;
import com.example.z1229.fragment.PluseFragment;
import com.example.z1229.fragment.UserFragment;
import com.example.z1229.receiver.SocketReceiver;
import com.example.z1229.service.SocketService;
import com.example.z1229.summerclient.R;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private ArrayList<Tab> mTabs = new ArrayList<Tab>(4);
    private LocalBroadcastManager localBroadcastManager;
    private SocketReceiver socketReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socketReceiver = new SocketReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.z1229.receiver.SocketReceiver");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(socketReceiver,intentFilter);

        initTab();
    }

    private void initTab() {
        //实例化4个Tab类的对象
        Tab Tab_home = new Tab(R.drawable.selector_home, R.string.home, HomeFragment.class);
        Tab Tab_user = new Tab(R.drawable.selector_user, R.string.user, UserFragment.class);
        Tab Tab_more = new Tab(R.drawable.selector_more, R.string.more, MoreFragment.class);
        Tab Tab_plus = new Tab(R.drawable.selector_plus, R.string.plus, PluseFragment.class);

        //将这4个对象加到一个List中
        mTabs.add(Tab_home);
        mTabs.add(Tab_more);
        mTabs.add(Tab_plus);
        mTabs.add(Tab_user);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realcontent);
        mInflater = LayoutInflater.from(this);

        //通过循环实例化一个个TabSpec
        //并调用其中setIndicator方法
        //然后将TabSpec加到TabHost中
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(String.valueOf(tab.getText()));
            tabSpec.setIndicator(buildView(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        //通过这行代码可以去除掉底部菜单4个图表之间的分割线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    //设置Indicator中的View
    private View buildView(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView Tab_img = (ImageView) view.findViewById(R.id.tab_img);
        TextView Tab_txt = (TextView) view.findViewById(R.id.tab_txt);

        Tab_img.setBackgroundResource(tab.getImage());
        Tab_txt.setText(tab.getText());
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(socketReceiver);
        Intent intent = new Intent(this,SocketService.class);
        stopService(intent);
    }
}
