package com.example.z1229.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.summerclient.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BloodAddActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,DatePickerDialog.OnDateSetListener {

    private ImageButton back,history;
    private TabLayout tabLayout;
    private SeekBar seekBar;
    private EditText mol;
    private Button save;
    private TextView b_era,date_text;
    private RelativeLayout relativeLayout;
    private String[] era = {"4.5-7.0","4.5-10.0"};
    private static final String[] tabText = {"早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡觉前"};
    private int mYear,mMonth,mDay;
    private Calendar ca = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_add);
        dbOpenHelper = new DBOpenHelper(this);
        initview();
    }

    private void initview(){
        tabLayout = (TabLayout)findViewById(R.id.blood_tab_add);
        back = (ImageButton)findViewById(R.id.btn_back);
        seekBar = (SeekBar)findViewById(R.id.blood_add_seek);
        mol = (EditText)findViewById(R.id.blood_add_edit);
        history = (ImageButton)findViewById(R.id.blood_add_history);
        save = (Button)findViewById(R.id.blood_add_save);
        b_era = (TextView)findViewById(R.id.blood_add_ear);
        date_text = (TextView)findViewById(R.id.blood_add_date_text);
        relativeLayout = (RelativeLayout)findViewById(R.id.blood_add_date);
        relativeLayout.setOnClickListener(this);
        save.setOnClickListener(this);
        history.setOnClickListener(this);
        back.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        b_era.setText(era[0]);
        for(String text:tabText){
            tabLayout.addTab(tabLayout.newTab().setText(text));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mol.setText("0.0");
                seekBar.setProgress(0);
                if(tab.getPosition()%2 ==0){
                    b_era.setText(era[0]);
                }else {
                    b_era.setText(era[1]);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        date_text.setText(simpleDateFormat.format(ca.getTime()));
    }

    private void showDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,mYear, mMonth, mDay);
        datePickerDialog.show();
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
                saveData();
                break;
            case R.id.blood_add_date:
                showDate();
                break;
            default:
                break;
        }
    }

    private void saveData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("保存后不可更改且会覆盖重复的记录，确认保存？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = String.valueOf(tabLayout.getSelectedTabPosition());
                String value = mol.getText().toString().trim();
                String date = date_text.getText().toString().trim();
                dbOpenHelper.save_b(dbOpenHelper,category,value,date);
                Toast.makeText(BloodAddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(tabLayout.getSelectedTabPosition()%2 == 0){
            if(progress<45){
                mol.setTextColor(this.getResources().getColor(R.color.yellow));
            }else if (progress<70){
                mol.setTextColor(this.getResources().getColor(R.color.green));
            }else {
                mol.setTextColor(this.getResources().getColor(R.color.red));
            }
        }else {
            if(progress<45){
                mol.setTextColor(this.getResources().getColor(R.color.yellow));
            }else if (progress<100){
                mol.setTextColor(this.getResources().getColor(R.color.green));
            }else {
                mol.setTextColor(this.getResources().getColor(R.color.red));
            }
        }
        mol.setText((float)progress/10+"");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        ca.set(year,month,dayOfMonth);
        if(ca.after(calendar)){
            Toast.makeText(this,"不能选择未来日期",Toast.LENGTH_SHORT).show();
            showDate();
        }else {
            date_text.setText(simpleDateFormat.format(ca.getTime()));
        }
    }
}
