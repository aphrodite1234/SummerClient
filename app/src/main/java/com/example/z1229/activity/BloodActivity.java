package com.example.z1229.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.z1229.adapter.XAxisValueFormatter;
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

import java.util.ArrayList;

public class BloodActivity extends AppCompatActivity implements View.OnClickListener,OnChartValueSelectedListener {

    private TabLayout tabLayout,tab;
    private ImageButton back;
    private LineChart lineChart;
    private static final String[] tabText = {"早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡觉前"};
//    private static final float[] range =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
        initview();
        initLineChart();
        initdata();
    }

    private void initview(){
        tabLayout = (TabLayout)findViewById(R.id.blood_tab);
        tab = (TabLayout)findViewById(R.id.blood_tab_1);
        back = (ImageButton)findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        for(String text:tabText){
            tabLayout.addTab(tabLayout.newTab().setText(text));
        }
        tab.addTab(tab.newTab().setText("最近7天"));
        tab.addTab(tab.newTab().setText("最近15天"));
        tab.addTab(tab.newTab().setText("最近30天"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(BloodActivity.this,tab.getText(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(BloodActivity.this,tab.getText(),Toast.LENGTH_SHORT).show();
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
        lineChart.setScaleEnabled(false);

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

        LimitLine limitLine = new LimitLine(10f,"10.0");
        limitLine.setLineColor(Color.RED);
        LimitLine limitLine1 = new LimitLine(4.5f,"4.5");
        limitLine1.setLineColor(Color.YELLOW);
        leftAxis.addLimitLine(limitLine);
        leftAxis.addLimitLine(limitLine1);
    }

    private void initdata(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("7-28");
        arrayList.add("7-29");
        arrayList.add("7-30");
        arrayList.add("7-31");
        arrayList.add("8-1");
        arrayList.add("8-2");
        arrayList.add("8-3");
        ArrayList<Entry> data = new ArrayList<>();
        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(arrayList);
        lineChart.getXAxis().setValueFormatter(xAxisValueFormatter);
        data.add(new Entry(0,6.7f));
        data.add(new Entry(1,4.6f));
        data.add(new Entry(2,5.0f));
        data.add(new Entry(3,5.5f));
        data.add(new Entry(4,6.1f));
        data.add(new Entry(5,10.6f));
        data.add(new Entry(6,2.6f));

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
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
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
        if(e.getY()<4.5){
            lineMarkerView.setColor(this.getResources().getColor(R.color.yellow));
        }else if(e.getY()<10){
            lineMarkerView.setColor(this.getResources().getColor(R.color.icon_more));
        }else {
            lineMarkerView.setColor(this.getResources().getColor(R.color.red));
        }
        lineChart.invalidate();
    }

    @Override
    public void onNothingSelected() {

    }
}
