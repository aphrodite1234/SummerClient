package com.example.z1229.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.adapter.ExpandableAdapter;
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.base.EatBase;
import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;
import com.example.z1229.utils.SPUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EatActivity extends AppCompatActivity implements View.OnClickListener ,DatePickerDialog.OnDateSetListener{

    private PieChart pieChart;
    private Button add;
    private TextView textView,textView1,date;
    private ImageButton imageButton,triangle,history;
    private ExpandableListView expandableListView;
    private static final int REQUEST_EAT_ADD = 1;
    private ArrayList<ArrayList<ItemInfo>> childData = new ArrayList<>();
    private ArrayList<String> groupData = new ArrayList<>();
    private ArrayList<ArrayList<String>> dbData = new ArrayList<>();
    private DBOpenHelper dbOpenHelper;
    private ExpandableAdapter expandableAdapter;
    private String[] eat = {"早餐(5:00-8:59)","上午加餐(9:00-11:59)","午餐(11:00-13:59)",
            "下午加餐(14:00-16:59)","晚餐(17:00-19:59)","晚上加餐(20:00-23:59)",};
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar ca = Calendar.getInstance();
    int mYear = ca.get(Calendar.YEAR);
    int mMonth = ca.get(Calendar.MONTH);
    int mDay = ca.get(Calendar.DAY_OF_MONTH);
    float cal=0;
    float cal_d = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);
        dbOpenHelper = new DBOpenHelper(this);
        initview();
        initPie();
        searchData();
    }

    private void initview(){
        pieChart = (PieChart)findViewById(R.id.eat_pie);
        imageButton = (ImageButton)findViewById(R.id.eat_btn_right);
        history = (ImageButton)findViewById(R.id.eat_btn_history);
        triangle = (ImageButton)findViewById(R.id.eat_triangle);
        add = (Button)findViewById(R.id.eat_btn_add);
        textView = (TextView)findViewById(R.id.eat_text);
        textView1 = (TextView)findViewById(R.id.eat_text_1);
        date = (TextView)findViewById(R.id.eat_text_date);
        expandableListView = (ExpandableListView)findViewById(R.id.eat_list);
        add.setOnClickListener(this);
        triangle.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        history.setOnClickListener(this);
        if(SPUtils.contains(this,"cal")){
            cal = Float.parseFloat(SPUtils.get(this,"cal",cal).toString());
            textView.setText(cal+"");
        }else {
            textView.setText("0.0");
        }
        textView1.setText("0.0");
        expandableAdapter = new ExpandableAdapter(this);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                v.setClickable(true);
                return true;
            }
        });
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
        pieChart.animateX(1000, Easing.EasingOption.EaseInOutQuad);//数据显示动画
        pieChart.getLegend().setEnabled(false);
    }

    private void initPieData(){
        pieChart.clear();
        int[] PIE_COLORS = { Color.rgb(181, 194, 202), Color.rgb(129, 216, 200)};
        ArrayList<PieEntry> entries = new ArrayList<>();
        Map<String, Float> pieValues = new HashMap<>();
        if(cal_d==0){
            pieValues.put("已摄入",0f);
            pieValues.put("未摄入",1f);
        }else if(cal_d>cal){
            pieValues.put("已摄入",cal_d);
            pieValues.put("未摄入",0f);
        }else {
            pieValues.put("已摄入",cal_d);
            pieValues.put("未摄入",cal-cal_d);
        }

        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entries.add(new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(7f);//设置饼块选中时偏离饼图中心的距离

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

    private void searchData(){
        cal_d = 0;
        groupData.clear();
        childData.clear();
        expandableListView.removeAllViewsInLayout();
        String date = simpleDateFormat.format(ca.getTime());
        dbData = dbOpenHelper.search_e(dbOpenHelper,date);
        if(dbData.size()>0){
            Type type = new TypeToken<ArrayList<ItemInfo>>() {}.getType();
            Gson gson = new Gson();
            for (ArrayList<String> s: dbData){
                groupData.add(eat[Integer.valueOf(s.get(0))]);
                ArrayList<ItemInfo> item = gson.fromJson(s.get(1),type);
                childData.add(item);
                cal_d+=Float.valueOf(s.get(2));
            }
            expandableAdapter.setData(groupData,childData);
            expandableListView.setAdapter(expandableAdapter);
            for (int i=0;i<groupData.size();i++){
                expandableListView.expandGroup(i);
            }
        }
        textView1.setText(cal_d+"");
        initPieData();
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
                Intent intent = new Intent(this,SportHistoryActivity.class);
                intent.putExtra("title","饮食摄入柱形图");
                startActivity(intent);
                break;
            case R.id.eat_btn_right:
                Intent intent1 = new Intent(this,EatCalActivity.class);
                startActivityForResult(intent1,1);
                break;
            default:
                break;
        }
    }

    private void showCalendar(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        ca.set(year,month,dayOfMonth);
        if(ca.after(calendar)){
            Toast.makeText(this,"不能选择未来日期",Toast.LENGTH_SHORT).show();
            showCalendar();
        }else {
            if(simpleDateFormat.format(ca.getTime()).equals(simpleDateFormat.format(calendar.getTime()))){
                date.setText("今天");
            }else {
                date.setText(simpleDateFormat.format(ca.getTime()));
            }
            searchData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            cal = data.getFloatExtra("cal",0f);
            textView.setText(cal+"");
            SPUtils.put(this,"cal",cal);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        searchData();
    }
}
