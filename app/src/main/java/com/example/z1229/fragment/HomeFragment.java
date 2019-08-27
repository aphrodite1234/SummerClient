package com.example.z1229.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.bean.Blog;
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
            ""};


    private BannerView mBannerView;
    private BannerViewPager mBannerViewPager;
    private View view;
    private RecyclerView mRecyclerView;
    private List<Blog> messageList,resmessagelist;
    private RecycleViewAdapter<Blog> messageRecycleViewAdapter;
    private LinearLayoutManager layoutManager;
    private DBOpenHelper dbOpenHelper;
    private boolean hasmore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        setupBannerView();
        initData();
        initRecyclerView();
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
        dbOpenHelper = new DBOpenHelper(getContext());
        //查询数据库得到messageList
        messageList=new ArrayList<>();
        for(int i=0;i<30;i++){//初始化30个
            Blog blog=new Blog();
            blog.setName(String.valueOf(i));
            messageList.add(blog);
        }
        if(messageList.size()>5){
            hasmore=true;
            resmessagelist=new ArrayList<>();
            for(int i=0;i<5;i++)
                resmessagelist.add(messageList.get(i));
        }else{
            hasmore=false;
            resmessagelist=messageList;
        }
    }

    public void initRecyclerView(){
        layoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        messageRecycleViewAdapter=new RecycleViewAdapter<Blog>(getActivity(),resmessagelist) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.item_home_social;
            }

            @Override
            public void bindView(RViewHolder holder, int position) {
                Blog blog=resmessagelist.get(position);

                if(blog!=null){
                    //holder.setImageBitmap(R.id.item_home_social_head,)
                    holder.setText(R.id.item_home_social_name,blog.getName());
                    if(position<resmessagelist.size()-1)
                    {
                        holder.getTextView(R.id.item_home_social_tip).setVisibility(View.GONE);
                    }else{
                        if(hasmore){
                            holder.getTextView(R.id.item_home_social_tip).setVisibility(View.VISIBLE);
                            holder.setText(R.id.item_home_social_tip,"--滑动加载更多--");
                        }
                        else{
                            holder.getTextView(R.id.item_home_social_tip).setVisibility(View.VISIBLE);
                            holder.setText(R.id.item_home_social_tip,"暂时没有更多,请稍后再试");
                        }
                    }
                }
                holder.getImageView(R.id.item_home_social_head).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {//点击头像跳转到个人界面
                        //startActivity(new Intent(getContext(),));
                    }
                });
                holder.getTextView(R.id.item_home_social_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//点击文字跳转到帖子详情

                    }
                });
                holder.getImageView(R.id.home_soacial_img1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.getImageView(R.id.home_soacial_img2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.getImageView(R.id.home_soacial_img3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        mRecyclerView.setAdapter(messageRecycleViewAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING||newState==RecyclerView.SCROLL_STATE_SETTLING){
                    //Glide.with(getContext()).pauseRequests();
                }else if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    if(layoutManager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1
                        && recyclerView.getChildCount()> 0){
                        updateData();
                        messageRecycleViewAdapter.notifyDataSetChanged();
                    }
                    //Glide.with(getContext()).resumeRequests();
                }
            }
        });
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
            GlideApp.with(getActivity()).load(mUrls[position]).placeholder( R.mipmap.zhaozu).error(R.mipmap.error2).centerCrop().into(bannerIv);
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
    private void updateData(){
        //重新查询数据库是否有新数据更新messageList

        int currentsize=resmessagelist.size();
        if(messageList.size()<=currentsize){
            hasmore=false;
        }else if(messageList.size()-currentsize>5){
            hasmore=true;
            for(int i=currentsize;i<currentsize+5;i++){
                resmessagelist.add(messageList.get(i));
            }
        }
        else{
            hasmore=false;
            for(int i=currentsize;i<messageList.size();i++){
                resmessagelist.add(messageList.get(i));
            }
        }
    }
}
