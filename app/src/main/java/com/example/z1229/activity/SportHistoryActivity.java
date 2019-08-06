package com.example.z1229.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.z1229.adapter.XAxisValueFormatter;
import com.example.z1229.base.LineMarkerView;
import com.example.z1229.summerclient.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_history);
        ButterKnife.bind(this);
        initview();
        initBarChart();
    }

    private void initview(){
        historyTab.addTab(historyTab.newTab().setText("最近15天"));
        historyTab.addTab(historyTab.newTab().setText("最近30天"));
        historyTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

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
        historyBar.setMaxVisibleValueCount(60);
        historyBar.setPinchZoom(false);
        historyBar.setDrawGridBackground(false);
        historyBar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        historyBar.getXAxis().setDrawGridLines(false);


        historyBar.getAxisLeft().setEnabled(false);

        initBardata(historyBar);
    }

    private void initBardata(BarChart mChart){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("7/21");
        arrayList.add("7/22");
        arrayList.add("7/23");
        arrayList.add("7/24");
        arrayList.add("7/25");
        arrayList.add("7/26");
        arrayList.add("7/27");
        arrayList.add("7/28");
        arrayList.add("7/29");
        arrayList.add("7/30");
        arrayList.add("7/31");
        arrayList.add("8/1");
        arrayList.add("8/2");
        arrayList.add("8/3");
        arrayList.add("8/4");
        ArrayList<BarEntry> data = new ArrayList<>();
        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(arrayList);
        mChart.getXAxis().setValueFormatter(xAxisValueFormatter);
        data.add(new BarEntry(0,600f));
        data.add(new BarEntry(1,400f));
        data.add(new BarEntry(2,500f));
        data.add(new BarEntry(3,550f));
        data.add(new BarEntry(4,610f));
        data.add(new BarEntry(5,106f));
        data.add(new BarEntry(6,260f));
        data.add(new BarEntry(7,600f));
        data.add(new BarEntry(8,400f));
        data.add(new BarEntry(9,500f));
        data.add(new BarEntry(10,550f));
        data.add(new BarEntry(11,610f));
        data.add(new BarEntry(12,106f));
        data.add(new BarEntry(13,260f));
        data.add(new BarEntry(14,260f));

        BarDataSet barDataSet = new BarDataSet(data,"能量/千卡");
        barDataSet.setDrawIcons(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);
        BarData data1 = new BarData(dataSets);
        data1.setValueTextSize(10f);
        data1.setBarWidth(0.9f);

        mChart.setData(data1);
        mChart.invalidate();
    }
}
