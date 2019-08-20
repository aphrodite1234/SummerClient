package com.example.z1229.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.z1229.adapter.XAxisValueFormatter;
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.base.LineMarkerView;
import com.example.z1229.summerclient.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BloodActivity extends AppCompatActivity implements View.OnClickListener,OnChartValueSelectedListener {

    private TabLayout tabLayout, itemtab;
    private ImageButton back,add;
    private LineChart lineChart;
    private DBOpenHelper dbOpenHelper;
    private static final String[] tabText = {"早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡觉前"};
    private ArrayList<ArrayList<String>> db_data = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
    private ArrayList<ArrayList<String>> xLists = new ArrayList<>();
    private ArrayList<ArrayList<Entry>> yLists = new ArrayList<>();
    private int[] item_count = {7,15,30};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
        dbOpenHelper = new DBOpenHelper(this);
        db_data = dbOpenHelper.search_b(dbOpenHelper,7);
        initData(7);
        initview();
        initLineChart();
        initLineData(7);
    }

    private void initview(){
        tabLayout = (TabLayout)findViewById(R.id.blood_tab);
        itemtab = (TabLayout)findViewById(R.id.blood_tab_1);
        back = (ImageButton)findViewById(R.id.btn_back);
        add = (ImageButton)findViewById(R.id.blood_add);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        for(String text:tabText){
            tabLayout.addTab(tabLayout.newTab().setText(text));
        }
        itemtab.addTab(itemtab.newTab().setText("最近7天"));
        itemtab.addTab(itemtab.newTab().setText("最近15天"));
        itemtab.addTab(itemtab.newTab().setText("最近30天"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                initLineData(item_count[itemtab.getSelectedTabPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        itemtab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                db_data = dbOpenHelper.search_b(dbOpenHelper,item_count[tab.getPosition()]);
                initData(item_count[tab.getPosition()]);
                initLineData(item_count[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initLineChart(){
        lineChart = (LineChart)findViewById(R.id.blood_line);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText("暂无数据");

        XAxis x = lineChart.getXAxis();
        x.setDrawGridLines(false);
        x.setDrawLabels(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.enableGridDashedLine(10f,10f,0f);
        leftAxis.setAxisMaximum(35f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setLabelCount(7);
    }

    private void initLineData(int count){
        int i = tabLayout.getSelectedTabPosition();
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        LimitLine limitLine,limitLine1;
        if(i%2==0){
            limitLine = new LimitLine(7f,"7.0");
            limitLine1 = new LimitLine(4.5f,"4.5");
        }else {
            limitLine = new LimitLine(10f,"10.0");
            limitLine1 = new LimitLine(4.5f,"4.5");
        }
        limitLine.setLineColor(Color.RED);
        limitLine1.setLineColor(Color.YELLOW);
        leftAxis.addLimitLine(limitLine);
        leftAxis.addLimitLine(limitLine1);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Entry> data = new ArrayList<>();

        arrayList = xLists.get(i);
        data = yLists.get(i);

        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(arrayList);
        lineChart.getXAxis().setValueFormatter(xAxisValueFormatter);

        LineDataSet lineDataSet = new LineDataSet(data,"血糖/mmol/L");
        lineDataSet.setCircleColor(Color.GREEN);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleRadius(2f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        ArrayList<ILineDataSet> datas = new ArrayList<>();
        datas.add(lineDataSet);
        LineData lineData = new LineData(datas);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateY(1000);
    }

    private void initData(int count){
        xLists.clear();
        yLists.clear();
        for (int i=0;i<7;i++){
            xLists.add(new ArrayList<String>());
            yLists.add(new ArrayList<Entry>());
        }
        for (int i=0;i<7;i++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,-count);
            for (int j=0;j<count;j++){
                calendar.add(Calendar.DAY_OF_MONTH,1);
                xLists.get(i).add(dateFormat.format(calendar.getTime()));
                yLists.get(i).add(new Entry(j,0));
            }
        }
        if (db_data.size()>0){
            for (ArrayList<String> item: db_data){
                int po = Integer.valueOf(item.get(0));
                float value = Float.valueOf(item.get(1));
                for (int i=0;i<xLists.get(po).size();i++){
                    if(xLists.get(po).get(i).equals(item.get(2).substring(5))){
                        yLists.get(po).set(i,new Entry(i,value));
                    }
                }

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.blood_add:
                startActivity(new Intent(this,BloodAddActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        LineMarkerView lineMarkerView = new LineMarkerView(this,lineChart.getXAxis().getValueFormatter(),"mmol/L");
        lineMarkerView.setChartView(lineChart);
        lineChart.setMarker(lineMarkerView);
        int i = tabLayout.getSelectedTabPosition();
        if(i%2 == 0){
            if(e.getY()<4.5){
                lineMarkerView.setColor(this.getResources().getColor(R.color.yellow));
            }else if(e.getY()<7.0){
                lineMarkerView.setColor(this.getResources().getColor(R.color.icon_more));
            }else {
                lineMarkerView.setColor(this.getResources().getColor(R.color.red));
            }
        }else {
            if(e.getY()<4.5){
                lineMarkerView.setColor(this.getResources().getColor(R.color.yellow));
            }else if(e.getY()<10){
                lineMarkerView.setColor(this.getResources().getColor(R.color.icon_more));
            }else {
                lineMarkerView.setColor(this.getResources().getColor(R.color.red));
            }
        }

        lineChart.invalidate();
    }

    @Override
    public void onNothingSelected() {

    }
}
