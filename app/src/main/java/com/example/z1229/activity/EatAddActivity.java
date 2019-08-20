package com.example.z1229.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.z1229.adapter.EatAdapter;
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.base.EatBase;
import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.ButterKnife;

public class EatAddActivity extends AppCompatActivity implements View.OnClickListener ,NumberPicker.OnValueChangeListener,DatePickerDialog.OnDateSetListener {

    private ImageButton save,back;
    private LinearLayout add;
    private RelativeLayout date,time;
    private TextView date_t,time_t,k;
    private RecyclerView recyclerView;
    private EatAdapter eatAdapter = new EatAdapter();
    private ArrayList<ItemInfo> arrayList = new ArrayList<>();
    private ArrayList<Integer[]> integers = new ArrayList<>();
    TextView textCal;
    NumberPicker pickerK;
    NumberPicker pickerB;
    NumberPicker pickerS;
    NumberPicker pickerG;
    Button btnCancel;
    Button btnOk;


    Calendar ca = Calendar.getInstance();
    int mYear = ca.get(Calendar.YEAR);
    int mMonth = ca.get(Calendar.MONTH);
    int mDay = ca.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private AlertDialog.Builder builder = null;
    private AlertDialog.Builder buildereat = null;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogeat;
    private View picker;
    private LayoutInflater inflater;
    private ItemInfo itemInfo;//正在选择的食物
    private int ke = 0;//选择的食物重量
    private int itemposition;
    String[] eat = {"早餐(5:00-8:59)","上午加餐(9:00-11:59)","午餐(11:00-13:59)",
            "下午加餐(14:00-16:59)","晚餐(17:00-19:59)","晚上加餐(20:00-23:59)",};
    private DBOpenHelper dbOpenHelper;
    int[] s = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_add);
        ButterKnife.bind(this);
        dbOpenHelper = new DBOpenHelper(this);
        initview();
        initDialog();
        showEat();
    }

    private void initview(){
        back = (ImageButton)findViewById(R.id.btn_back);
        save=(ImageButton)findViewById(R.id.eat_btn_save);
        add=(LinearLayout)findViewById(R.id.eat_add_add);
        date=(RelativeLayout)findViewById(R.id.eat_add_date);
        time = (RelativeLayout)findViewById(R.id.eat_add_time);
        date_t=(TextView)findViewById(R.id.eat_add_date_text);
        time_t=(TextView)findViewById(R.id.eat_add_time_text);
        k=(TextView)findViewById(R.id.eat_add_k);
        recyclerView = (RecyclerView)findViewById(R.id.eat_add_list);
        recyclerView.setAdapter(eatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eatAdapter.setOnItemClickListener(new EatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                textCal.setText("0");
                pickerK.setValue(0);
                pickerB.setValue(0);
                pickerS.setValue(0);
                pickerG.setValue(0);
                itemposition = position;
                itemInfo = arrayList.get(position);
                alertDialog.show();
            }
        });
        add.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        date_t.setText(dateFormat.format(ca.getTime()));
        time_t.setText(eat[0]);
    }

    private void initDialog() {
        inflater = EatAddActivity.this.getLayoutInflater();
        builder = new AlertDialog.Builder(EatAddActivity.this);
        picker = inflater.inflate(R.layout.dialog_eat_select, null, false);
        textCal = picker.findViewById(R.id.eat_select_dialog_cal);
        TextView point = picker.findViewById(R.id.dialog_eat_point);
        point.setVisibility(View.GONE);
        pickerG = picker.findViewById(R.id.eat_select_g);
        pickerS = picker.findViewById(R.id.eat_select_s);
        pickerB = picker.findViewById(R.id.eat_select_b);
        pickerK = picker.findViewById(R.id.eat_select_k);
        btnCancel = picker.findViewById(R.id.eat_select_dialog_cancel);
        btnOk = picker.findViewById(R.id.eat_select_dialog_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInfo.setKe(ke);
                eatAdapter.updateItem(itemposition, itemInfo);
                if(ke == 0){
                    integers.remove(itemposition);
                    arrayList.remove(itemposition);
                }
                ke=0;
                int cal=0;
                for (ItemInfo itemInfo:arrayList){
                    cal+=itemInfo.getCal()*itemInfo.getKe()/100;
                }
                k.setText(cal+"");
                alertDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(picker);
        builder.setCancelable(false);
        alertDialog = builder.create();
        initPicker(pickerK);
        initPicker(pickerB);
        initPicker(pickerS);
        initPicker(pickerG);
    }

    private void initPicker(NumberPicker numberPicker) {
        numberPicker.setOnValueChangedListener(this);
        numberPicker.setMaxValue(9);
        numberPicker.setMinValue(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.eat_btn_save:
                if (arrayList.size() > 0) {
                    saveData();
                }else {
                    Toast.makeText(EatAddActivity.this,"当前未选择食物",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.eat_add_time:
                alertDialogeat.show();
                break;
            case R.id.eat_add_date:
                showCalendar();
                break;
            case R.id.eat_add_add:
                Intent intent = new Intent(EatAddActivity.this,EatSelectActivity.class);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }

    private void showCalendar(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showEat(){
        buildereat=new AlertDialog.Builder(EatAddActivity.this);
        buildereat.setTitle("请选择用餐类型");
        buildereat.setSingleChoiceItems(eat, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s[0] =which;
            }
        });
        buildereat.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                time_t.setText(eat[s[0]]);
            }
        });
        buildereat.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        buildereat.setCancelable(false);
        alertDialogeat = buildereat.create();
    }

    private void saveData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("保存后不可更改且会覆盖重复的记录，确认保存？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Gson gson = new Gson();
                String cate = String.valueOf(s[0]);
                String item = gson.toJson(arrayList);
                String cal = k.getText().toString().trim();
                String date = date_t.getText().toString().trim();
                dbOpenHelper.save_e(dbOpenHelper,cate,item,cal,date);
                Toast.makeText(EatAddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                arrayList.clear();
                integers.clear();
                recyclerView.removeAllViews();
                k.setText("0");
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode){
            ArrayList<Integer[]> intList = (ArrayList<Integer[]>)data.getSerializableExtra("all");
            for (Integer[] x:intList){
                if (integers.size()>0){
                    ArrayList<Integer[]> integers1 = new ArrayList<>();
                    int i =0;
                    for (Integer[] y:integers){
                        if(x[0].equals(y[0]) && x[1].equals(y[1])){
                            integers.set(i,x);
                            break;
                        }
                        i++;
                        if (integers.size() == i){
                            integers1.add(x);
                        }
                    }
                    integers.addAll(integers1);
                }else {
                    integers.add(x);
                }
            }
            int cal=0;
            arrayList.clear();
            for (Integer[] x:integers){
                switch (x[0]){
                    case 0:
                        ItemInfo info = (new EatBase()).ZS.get(x[1]);
                        info.setKe(x[2]);
                        arrayList.add(info);
                        cal+=info.getCal()*x[2]/100;
                        break;
                    case 1:
                        ItemInfo info1 = (new EatBase()).RDN.get(x[1]);
                        info1.setKe(x[2]);
                        arrayList.add(info1);
                        cal+=info1.getCal()*x[2]/100;
                        break;
                    case 2:
                        ItemInfo info2 = (new EatBase()).SC.get(x[1]);
                        info2.setKe(x[2]);
                        arrayList.add(info2);
                        cal+=info2.getCal()*x[2]/100;
                        break;
                    case 3:
                        ItemInfo info3 = (new EatBase()).SG.get(x[1]);
                        info3.setKe(x[2]);
                        arrayList.add(info3);
                        cal+=info3.getCal()*x[2]/100;
                        break;
                    case 4:
                        ItemInfo info4 = (new EatBase()).ZL.get(x[1]);
                        info4.setKe(x[2]);
                        arrayList.add(info4);
                        cal+=info4.getCal()*x[2]/100;
                        break;
                    case 5:
                        ItemInfo info5 = (new EatBase()).YLLS.get(x[1]);
                        info5.setKe(x[2]);
                        arrayList.add(info5);
                        cal+=info5.getCal()*x[2]/100;
                        break;
                }
            }
            k.setText(cal+"");
            eatAdapter.setData(arrayList);
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        ke = pickerK.getValue()*1000+pickerB.getValue()*100+pickerS.getValue()*10+pickerG.getValue();
        textCal.setText(ke*itemInfo.getCal()/100+"");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        ca.set(year,month,dayOfMonth);
        if(ca.after(calendar)){
            Toast.makeText(this,"不能选择未来日期",Toast.LENGTH_SHORT).show();
            showCalendar();
        }else {
            date_t.setText(dateFormat.format(ca.getTime()));
        }
    }
}
