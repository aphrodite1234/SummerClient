package com.example.z1229.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EatCalActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.eat_cal_height_text)
    TextView eatCalHeightText;
    @BindView(R.id.eat_cal_height)
    RelativeLayout eatCalHeight;
    @BindView(R.id.eat_cal_weight_text)
    TextView eatCalWeightText;
    @BindView(R.id.eat_cal_weight)
    RelativeLayout eatCalWeight;
    @BindView(R.id.eat_cal_do_text)
    TextView eatCalDoText;
    @BindView(R.id.eat_cal_do)
    RelativeLayout eatCalDo;
    @BindView(R.id.eat_cal_btn)
    Button eatCalBtn;

    NumberPicker pickerK;
    NumberPicker pickerB;
    NumberPicker pickerS;
    NumberPicker pickerG;
    Button btnCancel;
    Button btnOk;
    RelativeLayout re;
    TextView ke;
    TextView point;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog;
    private View picker;
    private LayoutInflater inflater;

    private float cal = 0;
    private int height = 0;
    private int doing = 0;
    private float weight = 0;
    private boolean pressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_cal);
        ButterKnife.bind(this);
        initDialog();
    }

    @OnClick({R.id.btn_back, R.id.eat_cal_height, R.id.eat_cal_weight, R.id.eat_cal_do, R.id.eat_cal_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.eat_cal_height:
                pressed = true;
                pickerK.setVisibility(View.INVISIBLE);
                point.setVisibility(View.GONE);
                ke.setText("cm");
                alertDialog.show();
                break;
            case R.id.eat_cal_weight:
                pickerK.setVisibility(View.VISIBLE);
                point.setVisibility(View.VISIBLE);
                ke.setText("kg");
                alertDialog.show();
                break;
            case R.id.eat_cal_do:
                choice_do();
                break;
            case R.id.eat_cal_btn:
                Intent intent = new Intent(this, EatActivity.class);
                cal = calculate();
                intent.putExtra("cal", cal);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void initDialog() {
        inflater = EatCalActivity.this.getLayoutInflater();
        builder = new AlertDialog.Builder(EatCalActivity.this);
        picker = inflater.inflate(R.layout.dialog_eat_select, null, false);
        re = picker.findViewById(R.id.dialog_eat_re);
        re.setVisibility(View.GONE);
        ke = picker.findViewById(R.id.dialog_eat_ke);
        point = picker.findViewById(R.id.dialog_eat_point);
        pickerG = picker.findViewById(R.id.eat_select_g);
        pickerS = picker.findViewById(R.id.eat_select_s);
        pickerB = picker.findViewById(R.id.eat_select_b);
        pickerK = picker.findViewById(R.id.eat_select_k);
        btnCancel = picker.findViewById(R.id.eat_select_dialog_cancel);
        btnOk = picker.findViewById(R.id.eat_select_dialog_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pressed) {
                    height = pickerB.getValue() * 100 + pickerS.getValue() * 10 + pickerG.getValue();
                    eatCalHeightText.setText(height + " cm");
                    pressed = false;
                } else {
                    weight = (float) (pickerK.getValue() * 1000 + pickerB.getValue() * 100 + pickerS.getValue() * 10 + pickerG.getValue()) / 10;
                    eatCalWeightText.setText(weight + " kg");
                }
                alertDialog.dismiss();
                pickerK.setValue(0);
                pickerB.setValue(0);
                pickerS.setValue(0);
                pickerG.setValue(0);
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
        numberPicker.setMaxValue(9);
        numberPicker.setMinValue(0);
        numberPicker.setValue(0);
    }

    private void choice_do() {
        final int[] d = {0};
        final String[] item = {"休息状态", "轻体力劳动(公司职员，教师，售货员等)", "中体力劳动(学生，教师，家庭主妇等)", "重体力劳动(建筑工人，农民，运动员等)"};
        new AlertDialog.Builder(this)
                .setTitle("请选择劳动类型")
                .setSingleChoiceItems(item,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d[0] = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doing = d[0];
                        eatCalDoText.setText(item[doing]);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    private float calculate() {
        float count = 0;
        float weight = this.height - 105f;
        float wp = (this.weight - weight) / weight;
        if (wp >= 0.2) {
            switch (doing) {
                case 0:
                    count = weight * 15;
                    break;
                case 1:
                    count = weight * 25;
                    break;
                case 2:
                    count = weight * 30;
                    break;
                case 3:
                    count = weight * 35;
                    break;
            }
        } else if (wp >= -0.2) {
            switch (doing) {
                case 0:
                    count = weight * 20;
                    break;
                case 1:
                    count = weight * 30;
                    break;
                case 2:
                    count = weight * 35;
                    break;
                case 3:
                    count = weight * 40;
                    break;
            }
        } else {
            switch (doing) {
                case 0:
                    count = weight * 25;
                    break;
                case 1:
                    count = weight * 35;
                    break;
                case 2:
                    count = weight * 40;
                    break;
                case 3:
                    count = weight * 45;
                    break;
            }
        }
        return count;
    }

}
