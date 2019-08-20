package com.example.z1229.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.z1229.adapter.XAxisValueFormatter;
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.base.LineMarkerView;
import com.example.z1229.summerclient.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SportHistoryActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.history_title)
    TextView historyTitle;
    @BindView(R.id.history_tab)
    TabLayout historyTab;
    @BindView(R.id.history_bar)
    BarChart historyBar;

    private DBOpenHelper dbOpenHelper;
    private ArrayList<ArrayList<String>> dbData = new ArrayList<>();
    private int[] item = {15,30};
    String title;
    String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_history);
        dbOpenHelper = new DBOpenHelper(this);
        ButterKnife.bind(this);
        initview();
        initBarChart();
    }

    private void initview(){
        title=getIntent().getStringExtra("title");
        if(title.equals("饮食摄入柱形图")){
            label="饮食摄入能量/千卡";
            dbData = dbOpenHelper.search_e(dbOpenHelper,15);
        }else if(title.equals("运动消耗柱形图")){
            label="运动消耗能量/千卡";
            dbData = dbOpenHelper.search_s(dbOpenHelper,15);
        }
        historyTitle.setText(title);
        historyTab.addTab(historyTab.newTab().setText("最近15天"));
        historyTab.addTab(historyTab.newTab().setText("最近30天"));
        historyTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(title.equals("饮食摄入柱形图")){
                    dbData = dbOpenHelper.search_e(dbOpenHelper,item[tab.getPosition()]);
                    initBardata(historyBar,item[tab.getPosition()]);
                }else if(title.equals("运动消耗柱形图")){
                    dbData = dbOpenHelper.search_s(dbOpenHelper,item[tab.getPosition()]);
                    initBardata(historyBar,item[tab.getPosition()]);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void initBarChart(){
        historyBar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                LineMarkerView lineMarkerView = new LineMarkerView(SportHistoryActivity.this,historyBar.getXAxis().getValueFormatter(),"千卡");
                lineMarkerView.setChartView(historyBar);
                historyBar.setMarker(lineMarkerView);
                historyBar.invalidate();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        historyBar.setDrawBarShadow(false);
        historyBar.setDrawValueAboveBar(true);
        historyBar.getDescription().setEnabled(false);
        historyBar.setPinchZoom(false);
        historyBar.setDrawGridBackground(false);

        XAxis xAxis = historyBar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);

        YAxis yAxis = historyBar.getAxisRight();
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawAxisLine(true);

        historyBar.getAxisLeft().setEnabled(false);

        initBardata(historyBar,15);
        historyBar.setFitBars(true);
    }

    private void initBardata(BarChart mChart,int count){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        Calendar calendar=Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<BarEntry> data = new ArrayList<>();

        for (int i=0;i<count;i++){
            arrayList.add(dateFormat.format(calendar.getTime()));
            data.add(new BarEntry(i,0));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
        }
        for (ArrayList<String> item:dbData){
            float value = Float.valueOf(item.get(0));
            String date = item.get(1).substring(5);
            for (int j=0;j<arrayList.size();j++){
                if(date.equals(arrayList.get(j))){
                    data.set(j,new BarEntry(j,value));
                }
            }
        }

        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(arrayList);
        mChart.getXAxis().setValueFormatter(xAxisValueFormatter);

        BarDataSet barDataSet = new BarDataSet(data,label);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);
        BarData data1 = new BarData(dataSets);
        data1.setValueTextSize(10f);
        data1.setBarWidth(0.5f);

        mChart.setData(data1);
        historyBar.animateY(1000);
        mChart.invalidate();
    }
}
