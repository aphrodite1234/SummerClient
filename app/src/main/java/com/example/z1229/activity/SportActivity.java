package com.example.z1229.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.summerclient.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.sport_date)
    TextView sportDate;
    @BindView(R.id.sport_triangle)
    ImageButton sportTriangle;
    @BindView(R.id.sport_btn_history)
    ImageButton sportBtnHistory;
    @BindView(R.id.sport_text_1)
    TextView sportText1;
    @BindView(R.id.sport_btn_add)
    Button sportBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.sport_triangle, R.id.sport_btn_history, R.id.sport_btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.sport_triangle:
                showCalendar();
                break;
            case R.id.sport_btn_history:
                startActivity(new Intent(this,SportHistoryActivity.class));
                break;
            case R.id.sport_btn_add:
                break;
        }
    }

    private void showCalendar(){
        Calendar ca = Calendar.getInstance();
        int mYear = ca.get(Calendar.YEAR);
        int mMonth = ca.get(Calendar.MONTH);
        int mDay = ca.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Toast.makeText(this,year+"/"+(month+1)+"/"+dayOfMonth,Toast.LENGTH_SHORT).show();
    }
}
