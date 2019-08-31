package com.example.z1229.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.bean.UserBean;
import com.example.z1229.summerclient.R;
import com.example.z1229.utils.ACache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.NumberPicker;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * Created by MaiBenBen on 2019/8/28.
 */

public class UserSettingBody extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.user_setting_body_info_gender)
    LinearLayout linear_gender;
    @BindView(R.id.user_setting_body_info_gender_tv)
    TextView tv_gender;
    @BindView(R.id.user_setting_body_info_birthday)
    LinearLayout linear_birthday;
    @BindView(R.id.user_setting_body_info_birthday_tv)
    TextView tv_birthday;
    @BindView(R.id.user_setting_body_info_height)
    LinearLayout linear_height;
    @BindView(R.id.user_setting_body_info_height_tv)
    TextView tv_height;
    @BindView(R.id.user_setting_body_info_weight)
    LinearLayout linear_weight;
    @BindView(R.id.user_setting_body_info_weight_tv)
    TextView tv_weight;
    @BindView(R.id.user_setting_body_info_glycuresis)
    LinearLayout linear_glycuresis;
    @BindView(R.id.user_setting_body_info_glycuresis_tv)
    TextView tv_glycuresis;
    @BindView(R.id.user_setting_body_info_BMI_tv)
    TextView tv_bmi;
    @BindView(R.id.user_setting_body_info_back)
    ImageView iv_back;
    private ACache mACache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_setting_body);

        ButterKnife.bind(this);
        initView();
        initData();
    }
    public void initView(){
        linear_gender.setOnClickListener(this);
        linear_birthday.setOnClickListener(this);
        linear_glycuresis.setOnClickListener(this);
        linear_height.setOnClickListener(this);
        linear_weight.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    public void initData(){
        mACache = ACache.get(this);
        UserBean userBean=(UserBean)mACache.getAsObject("local_userBean");
        tv_gender.setText(userBean.getSex());
        tv_birthday.setText(userBean.getBirthday());
        tv_glycuresis.setText(userBean.getType());
        tv_height.setText(userBean.getHeight());
        tv_weight.setText(userBean.getWeight());
        tv_bmi.setText(userBean.getBmi());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_setting_body_info_gender:
                //setSex();
                onGenderPicker();
                break;
            case R.id.user_setting_body_info_birthday:
                onYearMonthDayPicker();
                break;
            case R.id.user_setting_body_info_height:
                onHeightPicker();
                break;
            case R.id.user_setting_body_info_weight:
                onWeightPicker();
                break;
            case R.id.user_setting_body_info_glycuresis:
                onGluycuresisPicker();
                break;
            case R.id.user_setting_body_info_back:
                finish();
                break;
        }
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(1919, 1, 1);
        picker.setRangeEnd(2019, 8, 30);
        picker.setSelectedItem(1995, 6, 15);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                //Toast.makeText(UserSettingBody.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                tv_birthday.setText(String.format("%s-%s-%s", year, month, day));
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    public void setBMI(){
        String str_heigth=tv_height.getText().toString();
        String str_weigth=tv_weight.getText().toString();
        float height= Float.valueOf(str_heigth.substring(0, str_heigth.length() - 2)) / 100;
        float weigth=Float.valueOf(str_weigth.substring(0,str_weigth.length()-2));
        //float bmi= (float) Math.pow(weigth/height,2);
        float bmi=weigth/(height*height);
        String str_bmi=String.format(Locale.CHINA,"%1.0f",bmi);
        tv_bmi.setText(str_bmi);
    }

    public void onHeightPicker() {
        NumberPicker picker = new NumberPicker(this);
        picker.setCanLoop(false);
        picker.setLineVisible(false);
        picker.setWheelModeEnable(true);
        picker.setOffset(2);//偏移量
        picker.setRange(145, 200, 1);//数字范围
        picker.setSelectedItem(172);
        picker.setLabel("厘米");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                //Toast.makeText(UserSettingBody.this, "index=" + index + ", item=" + item.intValue(), Toast.LENGTH_SHORT).show();
                tv_height.setText(String.format("%s厘米", String.valueOf(item.intValue())));
                if(!TextUtils.isEmpty(tv_weight.getText()))
                    setBMI();
            }
        });
        picker.show();
    }

    public void onWeightPicker() {
        NumberPicker picker = new NumberPicker(this);
        picker.setCanLoop(false);
        picker.setLineVisible(false);
        picker.setWheelModeEnable(true);
        picker.setOffset(2);//偏移量
        picker.setRange(40, 200, 1);//数字范围
        picker.setSelectedItem(50);
        picker.setLabel("千克");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                //Toast.makeText(UserSettingBody.this, "index=" + index + ", item=" + item.intValue(), Toast.LENGTH_SHORT).show();
                tv_weight.setText(String.format("%s公斤", String.valueOf(item.intValue())));
                if(!TextUtils.isEmpty(tv_height.getText()))
                    setBMI();
            }
        });
        picker.show();
    }

    public void onGenderPicker() {
        ArrayList<String> list = new ArrayList<>();
        list.add("男");list.add("女");
        SinglePicker<String> picker = new SinglePicker<>(this, list);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setTextSize(18);
        picker.setSelectedIndex(8);
        picker.setItemWidth(200);
        picker.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);
        /*picker.setSelectedTextColor(Color.GREEN);//前四位值是透明度
        picker.setUnSelectedTextColor(Color.RED);*/
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {

            }
        });
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                Toast.makeText(UserSettingBody.this, "index=" + index + ", item=" + item, Toast.LENGTH_SHORT).show();
                tv_gender.setText(item);
            }
        });
        picker.show();
    }

    public void onGluycuresisPicker() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Ⅰ型糖尿病");list.add("Ⅱ型糖尿病");
        list.add("妊娠糖尿病");list.add("继发性糖尿病");
        list.add("特殊类型的糖尿病");list.add("其他");
        SinglePicker<String> picker = new SinglePicker<>(this, list);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setTextSize(18);
        picker.setSelectedIndex(8);
        picker.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);
        picker.setItemWidth(200);
    /*    picker.setSelectedTextColor(Color.GREEN);//前四位值是透明度
        picker.setUnSelectedTextColor(Color.RED);*/
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {

            }
        });
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                Toast.makeText(UserSettingBody.this, "index=" + index + ", item=" + item, Toast.LENGTH_SHORT).show();
                tv_glycuresis.setText(item);
            }
        });
        picker.show();
    }

    @Override
    public void onDestroy(){
        UserBean userBean=(UserBean)mACache.getAsObject("local_userBean");
        userBean.setSex(tv_gender.getText().toString());
        userBean.setBirthday(tv_birthday.getText().toString());
        userBean.setType(tv_glycuresis.getText().toString());
        userBean.setHeight(tv_height.getText().toString());
        userBean.setWeight(tv_weight.getText().toString());
        userBean.setBmi(tv_bmi.getText().toString());
        mACache.put("local_userBean",userBean);
        super.onDestroy();
    }
}
