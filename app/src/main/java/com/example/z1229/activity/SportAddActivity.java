package com.example.z1229.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.z1229.adapter.SportAdapter;
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.base.EatBase;
import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SportAddActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,NumberPicker.OnValueChangeListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

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

    private ArrayList<ItemInfo> arrayList = (new EatBase()).SPORT;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;
    private View view;
    private ListView listView;
    private SportAdapter sportAdapter;
    private int min = 0;
    private int cal = 0;
    private String date,time;

    NumberPicker pickerK;
    NumberPicker pickerB;
    NumberPicker pickerS;
    NumberPicker pickerG;
    Button btnCancel;
    Button btnOk;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    Calendar ca = Calendar.getInstance();
    int mYear = ca.get(Calendar.YEAR);
    int mMonth = ca.get(Calendar.MONTH);
    int mDay = ca.get(Calendar.DAY_OF_MONTH);
    int c_year;
    int c_month;
    int c_day;
    private DBOpenHelper dbOpenHelper;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_add);
        dbOpenHelper = new DBOpenHelper(this);
        ButterKnife.bind(this);
        inflater = SportAddActivity.this.getLayoutInflater();
        builder = new AlertDialog.Builder(SportAddActivity.this);
    }

    @OnClick({R.id.btn_back, R.id.sport_add_btn_save, R.id.sport_add_type, R.id.sport_add_long, R.id.sport_add_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.sport_add_btn_save:
                if(min*cal!=0){
                    saveData();
                }else {
                    Toast.makeText(this,"请选择运动类型或时长",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sport_add_type:
                initType();
                break;
            case R.id.sport_add_long:
                initLong();
                break;
            case R.id.sport_add_time:
                showDate();
                break;
        }
    }

    private void initType(){
        view = inflater.inflate(R.layout.dialog_sport_select, null, false);
        listView= view.findViewById(R.id.dialog_sport_list);
        sportAdapter = new SportAdapter(this,arrayList);
        listView.setAdapter(sportAdapter);
        listView.setOnItemClickListener(this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
        WindowManager.LayoutParams  lp= alertDialog.getWindow().getAttributes();
        lp.width = 900;
        alertDialog.getWindow().setAttributes(lp);
    }

    private void initLong() {
        view = inflater.inflate(R.layout.dialog_eat_select, null, false);
        view.findViewById(R.id.dialog_eat_re).setVisibility(View.GONE);
        TextView textView = view.findViewById(R.id.dialog_eat_ke);
        textView.setText("min");
        TextView point = view.findViewById(R.id.dialog_eat_point);
        point.setVisibility(View.GONE);
        pickerG = view.findViewById(R.id.eat_select_g);
        pickerS = view.findViewById(R.id.eat_select_s);
        pickerB = view.findViewById(R.id.eat_select_b);
        pickerK = view.findViewById(R.id.eat_select_k);
        pickerK.setVisibility(View.INVISIBLE);
        btnCancel = view.findViewById(R.id.eat_select_dialog_cancel);
        btnOk = view.findViewById(R.id.eat_select_dialog_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportAddLongText.setText(min+" min");
                sportAddK.setText(cal * min/60+"");
                alertDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        initPicker(pickerB);
        initPicker(pickerS);
        initPicker(pickerG);
        alertDialog.show();
    }
    private void saveData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("保存后不可更改且会覆盖重复的记录，确认保存？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Gson gson = new Gson();
                ItemInfo info = arrayList.get(position);
                info.setKe(min);
                String cate = gson.toJson(info);
                String s_cal = sportAddK.getText().toString().trim();
                String s_time = time;
                String s_date = date;
                dbOpenHelper.save_s(dbOpenHelper,cate,s_cal,s_date,s_time);
                Toast.makeText(SportAddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
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

    private void initPicker(NumberPicker numberPicker) {
        numberPicker.setOnValueChangedListener(this);
        numberPicker.setMaxValue(9);
        numberPicker.setMinValue(0);
    }

    private void showDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTime(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,this,8,0,true);
        timePickerDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        sportAddTypeText.setText(arrayList.get(position).getName());
        cal = arrayList.get(position).getCal();
        sportAddK.setText(cal * min/60+"");
        alertDialog.dismiss();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        min=pickerB.getValue()*100+pickerS.getValue()*10+pickerG.getValue();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c_year = year;
        c_month = month;
        c_day = dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        ca.set(year,month,dayOfMonth);
        if(ca.after(calendar)){
            Toast.makeText(this,"不能选择未来日期",Toast.LENGTH_SHORT).show();
            showDate();
        }else {
            date = dateFormat.format(ca.getTime());
            showTime();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        ca.set(c_year,c_month,c_day,hourOfDay,minute);
        time = timeFormat.format(ca.getTime());
        if(ca.after(calendar)) {
            Toast.makeText(this, "不能选择未来时间", Toast.LENGTH_SHORT).show();
            ca.clear();
            showTime();
        }else {
            sportAddTimeText.setText(date+" "+time);
        }
    }
}
