package com.example.z1229.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.z1229.summerclient.R;

import butterknife.ButterKnife;

public class EatAddActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton save,back;
    private LinearLayout add;
    private RelativeLayout date,time;
    private TextView date_t,time_t,k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_add);
        ButterKnife.bind(this);
    }

    private void initview(){
        back = (ImageButton)findViewById(R.id.btn_back);
        save=(ImageButton)findViewById(R.id.eat_btn_save);
        add=(LinearLayout)findViewById(R.id.eat_add);
        date=(RelativeLayout)findViewById(R.id.eat_add_date);
        time = (RelativeLayout)findViewById(R.id.eat_add_time);
        date_t=(TextView)findViewById(R.id.eat_add_date_text);
        time_t=(TextView)findViewById(R.id.eat_add_time_text);
        k=(TextView)findViewById(R.id.eat_add_k);
        add.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.eat_btn_save:
                break;
            case R.id.eat_add_time:
                break;
            case R.id.eat_add_date:
                break;
            case R.id.eat_add:
                break;
            default:
                break;
        }
    }
}
