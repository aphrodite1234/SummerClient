package com.example.z1229.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.adapter.SportAdapter;
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @BindView(R.id.sport_list)
    ListView sportList;


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar ca = Calendar.getInstance();
    int mYear = ca.get(Calendar.YEAR);
    int mMonth = ca.get(Calendar.MONTH);
    int mDay = ca.get(Calendar.DAY_OF_MONTH);
    DBOpenHelper dbOpenHelper;
    String date = simpleDateFormat.format(ca.getTime());
    int cal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        dbOpenHelper = new DBOpenHelper(this);
        ButterKnife.bind(this);
        initList();
    }

    public void initList(){
        cal=0;
        ArrayList<ArrayList<String>> db_data = dbOpenHelper.search_s(dbOpenHelper,date);
        ArrayList<String> a_date = new ArrayList<>();
        ArrayList<ItemInfo> item = new ArrayList<>();
        Gson gson = new Gson();
        for (ArrayList<String> s:db_data){
            ItemInfo info = gson.fromJson(s.get(0),ItemInfo.class);
            item.add(info);
            cal += Integer.valueOf(s.get(1));
            a_date.add(s.get(3));
        }
        SportAdapter sportAdapter = new SportAdapter(this,item,a_date);
        sportList.setAdapter(sportAdapter);
        sportText1.setText(cal+"");
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
                Intent intent = new Intent(this, SportHistoryActivity.class);
                intent.putExtra("title", "运动消耗柱形图");
                startActivity(intent);
                break;
            case R.id.sport_btn_add:
                startActivity(new Intent(this, SportAddActivity.class));
                break;
        }
    }

    private void showCalendar() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        ca.set(year, month, dayOfMonth);
        if (ca.after(calendar)) {
            Toast.makeText(this, "不能选择未来日期", Toast.LENGTH_SHORT).show();
            showCalendar();
        } else {
            if (simpleDateFormat.format(ca.getTime()).equals(simpleDateFormat.format(calendar.getTime()))) {
                sportDate.setText("今天");
            } else {
                sportDate.setText(simpleDateFormat.format(ca.getTime()));
            }
            date = simpleDateFormat.format(ca.getTime());
            initList();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initList();
    }
}
