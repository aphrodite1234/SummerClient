package com.example.z1229.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.z1229.activity.BloodAddActivity;
import com.example.z1229.adapter.RViewHolder;
import com.example.z1229.adapter.RecycleViewAdapter;
import com.example.z1229.summerclient.R;
import com.example.z1229.view.BannerView.BannerAdapter;
import com.example.z1229.view.BannerView.BannerView;
import com.example.z1229.view.BannerView.BannerViewPager;
import com.example.z1229.view.BannerView.GlideApp;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private String[] mUrls = {"http://pb9.pstatp.com/origin/24990000d4c26180d691",
            "http://pb9.pstatp.com/origin/1dcf002c646ac321e698",
            "http://pb9.pstatp.com/origin/1dcf002c646ac321e698"};


    private BannerView mBannerView;
    private BannerViewPager mBannerViewPager;
    private View view;
    private RecyclerView mRecyclerView;
    private List<Message> messageList;
    private RecycleViewAdapter<Message> messageRecycleViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        setupBannerView();
        return view;
    }

    /*@Override
    public void onStart(){
        super.onStart();
        initView();
        setupBannerView();
    }*/
    public void initView(){
        mBannerView=view.findViewById(R.id.home_bannerview);
        view.findViewById(R.id.home_add_record).setOnClickListener(this);
        view.findViewById(R.id.home_GI_value).setOnClickListener(this);
        view.findViewById(R.id.home_science_diet).setOnClickListener(this);
        view.findViewById(R.id.home_wiki).setOnClickListener(this);
        mRecyclerView=view.findViewById(R.id.home_social_recycle);
    }
    private void setupBannerView() {
        mBannerViewPager = mBannerView.getBannerViewPager();
        mBannerView.setAdapter(mBannerAdapter);
        mBannerView.setScrollerDuration(1288);
        mBannerView.startLoop();
    }

    public void initData(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        messageList=new ArrayList<>(3);
        mRecyclerView.setLayoutManager(layoutManager);
        messageRecycleViewAdapter=new RecycleViewAdapter<Message>(getActivity(),messageList) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.item_home_social;
            }

            @Override
            public void bindView(RViewHolder holder, int position) {
                Message message=messageList.get(position);
                if(message!=null){
                    //holder.setImageResource(message.getImg);
                    //holder.setText(R.id.);
                }
            }
        };
        for(int i=0;i<messageList.size();i++)
            messageRecycleViewAdapter.notifyItemChanged(i);
    }

    private BannerAdapter mBannerAdapter = new BannerAdapter() {
        @Override
        public View getView(int position, View convertView) {
            ImageView bannerIv;
            if (convertView == null) {
                bannerIv = new ImageView(getActivity());
                bannerIv.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                bannerIv = (ImageView) convertView;
            }
            GlideApp.with(getActivity()).load(mUrls[position]).into(bannerIv);
            return bannerIv;
        }

        @Override
        public int getCount() {
            return mUrls.length;
        }
    };
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
}
