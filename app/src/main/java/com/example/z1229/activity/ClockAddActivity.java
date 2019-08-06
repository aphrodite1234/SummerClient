package com.example.z1229.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.z1229.summerclient.R;

import java.util.ArrayList;
import java.util.Collections;

public class ClockAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private ImageButton back, save;
    private RelativeLayout repeat;
    private TextView textView,add_text;
    private LinearLayout delete;
    private TimePicker timePicker;
    private AlertDialog.Builder builder;
    private Intent intent;
    private Bundle bundle = new Bundle();
    private Bundle bundlein = new Bundle();
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private Intent i;
    private static final int INTENT_CLOCK_ADD = 1;
    private static final int INTENT_CLOCK_ITEM = 2;
    private static final int INTENT_CLOCK_DELETE = 3;
    private int position;
    private static final String[] WEEK = {"周日","周一", "周二", "周三", "周四", "周五", "周六"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_add);
        initview();
    }

    private void initview() {
        editText = (EditText) findViewById(R.id.add_edit);
        back = (ImageButton) findViewById(R.id.btn_back);
        save = (ImageButton) findViewById(R.id.btn_clock_save);
        repeat = (RelativeLayout) findViewById(R.id.add_repeat);
        delete = (LinearLayout) findViewById(R.id.add_delete);
        textView = (TextView) findViewById(R.id.add_repeat_text);
        add_text = (TextView)findViewById(R.id.add_text);
        timePicker = (TimePicker) findViewById(R.id.add_clock);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        repeat.setOnClickListener(this);
        delete.setOnClickListener(this);

        timePicker.setIs24HourView(true);
        intent = new Intent(ClockAddActivity.this,ClockActivity.class);
//        i=getIntent();
//        bundlein = i.getExtras();
//        int type = bundlein.getInt("type");
//        if(type == INTENT_CLOCK_ITEM){
//            add_text.setText("修改提醒");
//            delete.setVisibility(View.VISIBLE);
//            position = bundlein.getInt("position");
//            editText.setText(bundlein.getString("remark"));
//            textView.setText(bundlein.getString("repeat"));
////            timePicker.setHour(bundlein.getInt("h"));
////            timePicker.setMinute(bundlein.getInt("m"));
//        }else {
//            delete.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clock_save:
                bundle.putInt("type",0);
                bundle.putInt("position",position);
                bundle.putInt("h",timePicker.getCurrentHour());
                bundle.putInt("m",timePicker.getCurrentMinute());
                bundle.putString("remark",editText.getText().toString());
                bundle.putString("repeat",textView.getText().toString());
                bundle.putIntegerArrayList("arrayList",arrayList);
                intent.putExtras(bundle);
                if(setSystemAlarmClock(this,editText.getText().toString(),timePicker.getCurrentHour(),timePicker.getCurrentMinute())){
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    Toast.makeText(this,"创建失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_repeat:
                showMultiSelect();
                break;
            case R.id.add_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bundle.putInt("type",INTENT_CLOCK_DELETE);
                        bundle.putInt("position",position);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("确定删除这个提醒？");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);

                break;
            default:
                break;
        }
    }

    private void showMultiSelect() {
        final ArrayList<Integer> choice = new ArrayList<>();

        //默认都未选中
        boolean[] isSelect = {false, false, false, false, false, false, false};

        builder = new AlertDialog.Builder(this)
                .setMultiChoiceItems(WEEK, isSelect, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            choice.add(i);
                        } else {
                            choice.remove((Integer)i);
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Collections.sort(choice);
                        arrayList = choice;
                        StringBuilder str = new StringBuilder();
                        if (0 == choice.size()){
                            textView.setText("只响一次");
                        }else if (7 == choice.size()) {
                            textView.setText("每天");
                        } else {
                            for (int j = 0; j < choice.size(); j++) {
                                str.append(WEEK[choice.get(j)]);
                                if(j < choice.size()){
                                    str.append(" ");
                                }
                            }
                            textView.setText(str);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = builder.create();

        TextView title = new TextView(this);
        title.setText("重复");
        title.setPadding(10, 30, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(18);
        title.setTextColor(Color.BLACK);
        alertDialog.setCustomTitle(title);

        alertDialog.show();
        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

    public boolean setSystemAlarmClock(Context context, String message, int hour, int minute) {
        if (Build.VERSION.SDK_INT < 9) {
            return false;
        }
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        if (Build.VERSION.SDK_INT >= 11) {
            intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
        }
        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
