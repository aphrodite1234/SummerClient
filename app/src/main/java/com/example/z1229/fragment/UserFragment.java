package com.example.z1229.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.z1229.activity.UserFragmentPlus;
import com.example.z1229.activity.UserFragmentSetting;
import com.example.z1229.adapter.UserPagerAdapter;
import com.example.z1229.summerclient.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserFragment extends Fragment{

    private List<Fragment> fragmentList;
    private List<String> tabList;
    @BindView(R.id.fragment_user_tabs)
    TabLayout tabLayout;
    @BindView(R.id.fragment_user_viewpager)
    ViewPager viewPager;
    private View view;
    private FragmentManager fm=getFragmentManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this,view);
        initViewPager();
        return view;
    }


    public void initViewPager(){
        fragmentList = new ArrayList<>();
        tabList = new ArrayList<>();
        fragmentList.add(UserFragmentSetting.newInstance(0));
        fragmentList.add(UserFragmentPlus.newInstance(0));
        tabList.add("设置");
        tabList.add("我的动态");
        viewPager.setAdapter(new UserPagerAdapter(getChildFragmentManager(),getActivity(),fragmentList,tabList));
        tabLayout.setupWithViewPager(viewPager);//此方法就是让tablayout和ViewPager联动
    }
}
