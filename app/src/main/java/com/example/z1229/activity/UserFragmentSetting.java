package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.z1229.summerclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MaiBenBen on 2019/8/27.
 */

public class UserFragmentSetting extends Fragment implements View.OnClickListener{
    private View view;
    @BindView(R.id.fragment_user_setting_body_info)
    LinearLayout Linear_body;
    @BindView(R.id.fragment_user_setting_personal_info)
    LinearLayout Linear_personal;
    @BindView(R.id.fragment_user_setting_follow_info)
    LinearLayout Linear_follow;

    public static UserFragmentSetting newInstance(int index) {
        UserFragmentSetting f = new UserFragmentSetting();
        /*Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);*/
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user_setting, container, false);
        ButterKnife.bind(this,view);
        initData();
        return view;
    }

    public void initData(){
        view.findViewById(R.id.fragment_user_setting_personal_info).setOnClickListener(this);
        view.findViewById(R.id.fragment_user_setting_body_info).setOnClickListener(this);
        view.findViewById(R.id.fragment_user_setting_follow_info).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_user_setting_body_info:
                startActivity(new Intent(getActivity(),UserSettingBody.class));
                break;
            case R.id.fragment_user_setting_personal_info:
                startActivity(new Intent(getActivity(),UserSettingPersonal.class));
                break;
            case R.id.fragment_user_setting_follow_info:
                //startActivity(new Intent(getActivity(),UserSetting.class));
                break;
        }
    }
}
