package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.z1229.summerclient.R;

public class BloodAddActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    private ImageButton back,history;
    private TabLayout tabLayout;
    private SeekBar seekBar;
    private EditText mol,remark;
    private Button save;
    private TextView normal,time;
    private static final String[] tabText = {"早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡觉前"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_add);
        initview();
    }

    private void initview(){
        tabLayout = (TabLayout)findViewById(R.id.blood_tab_add);
        back = (ImageButton)findViewById(R.id.btn_back);
        seekBar = (SeekBar)findViewById(R.id.blood_add_seek);
        mol = (EditText)findViewById(R.id.blood_add_edit);
        remark = (EditText)findViewById(R.id.blood_add_remark);
        history = (ImageButton)findViewById(R.id.blood_add_history);
        save = (Button)findViewById(R.id.blood_add_save);
        normal = (TextView)findViewById(R.id.blood_add_normal);
        time = (TextView)findViewById(R.id.blood_add_time);
        save.setOnClickListener(this);
        history.setOnClickListener(this);
        back.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        for(String text:tabText){
            tabLayout.addTab(tabLayout.newTab().setText(text));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.blood_add_history:
                Intent intent = new Intent(this,BloodActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.blood_add_save:
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(progress<45){
            mol.setTextColor(this.getResources().getColor(R.color.yellow));
        }else if (progress<100){
            mol.setTextColor(this.getResources().getColor(R.color.green));
        }else {
            mol.setTextColor(this.getResources().getColor(R.color.red));
        }
        mol.setText((float)progress/10+"");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
