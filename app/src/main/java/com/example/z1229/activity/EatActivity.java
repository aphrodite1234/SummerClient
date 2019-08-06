package com.example.z1229.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.summerclient.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.mob.MobSDK.getContext;

public class EatActivity extends AppCompatActivity implements View.OnClickListener ,DatePickerDialog.OnDateSetListener{

    private PieChart pieChart;
    private Button add;
    private TextView textView,textView1;
    private ImageButton imageButton,triangle,history;
    private static final int REQUEST_EAT_ADD = 1;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);
        initview();
        initPie();
        initdata();
    }

    private void initview(){
        pieChart = (PieChart)findViewById(R.id.eat_pie);
        imageButton = (ImageButton)findViewById(R.id.eat_btn_right);
        history = (ImageButton)findViewById(R.id.eat_btn_history);
        triangle = (ImageButton)findViewById(R.id.eat_triangle);
        add = (Button)findViewById(R.id.eat_btn_add);
        textView = (TextView)findViewById(R.id.eat_text);
        textView1 = (TextView)findViewById(R.id.eat_text_1);
        date = (TextView)findViewById(R.id.eat_add_date_text);
        add.setOnClickListener(this);
        triangle.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        history.setOnClickListener(this);
    }

    private void initPie(){
        pieChart.setUsePercentValues(true);//设置使用百分比
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setExtraOffsets(10, 0, 10, 0);
        pieChart.setCenterTextSize(10f);//设置环中文字的大小
        pieChart.setDrawCenterText(true);//设置绘制环中文字
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setRotationAngle(120f);//设置旋转角度
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.animateX(1500, Easing.EasingOption.EaseInOutQuad);//数据显示动画
        pieChart.getLegend().setEnabled(false);
    }

    private void initdata(){
        int[] PIE_COLORS = { Color.rgb(181, 194, 202), Color.rgb(129, 216, 200)};
        ArrayList<PieEntry> entries = new ArrayList<>();
        Map<String, Float> pieValues = new HashMap<>();
        pieValues.put("已摄入",60f);
        pieValues.put("未摄入",40f);

        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entries.add(new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(5f);//设置饼块选中时偏离饼图中心的距离

        dataSet.setColors(PIE_COLORS);//设置饼块的颜色
        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.BLUE);//设置连接线的颜色
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.eat_triangle:
                showCalendar();
                break;
            case R.id.eat_btn_add:
                Intent eat_add = new Intent(this,EatAddActivity.class);
                startActivityForResult(eat_add,REQUEST_EAT_ADD);
                break;
            case R.id.eat_btn_history:
                startActivity(new Intent(this,SportHistoryActivity.class));
                break;
            case R.id.eat_btn_right:
                Toast.makeText(this,"设置饮食摄入",Toast.LENGTH_SHORT).show();
                break;
            default:
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
