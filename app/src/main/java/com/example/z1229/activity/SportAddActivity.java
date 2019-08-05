package com.example.z1229.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.z1229.summerclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SportAddActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.sport_add_btn_save)
    ImageButton sportAddBtnSave;
    @BindView(R.id.sport_add_k)
    TextView sportAddK;
    @BindView(R.id.sport_add_type_text)
    TextView sportAddTypeText;
    @BindView(R.id.sport_add_type)
    RelativeLayout sportAddType;
    @BindView(R.id.sport_add_long_text)
    TextView sportAddLongText;
    @BindView(R.id.sport_add_long)
    RelativeLayout sportAddLong;
    @BindView(R.id.sport_add_time_text)
    TextView sportAddTimeText;
    @BindView(R.id.sport_add_time)
    RelativeLayout sportAddTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_add);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.sport_add_btn_save, R.id.sport_add_type, R.id.sport_add_long, R.id.sport_add_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.sport_add_btn_save:
                break;
            case R.id.sport_add_type:
                break;
            case R.id.sport_add_long:
                break;
            case R.id.sport_add_time:
                break;
        }
    }
}
