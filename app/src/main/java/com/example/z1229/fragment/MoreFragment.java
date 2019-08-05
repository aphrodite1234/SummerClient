package com.example.z1229.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.z1229.activity.BloodActivity;
import com.example.z1229.activity.BloodAddActivity;
import com.example.z1229.activity.EatActivity;
import com.example.z1229.activity.EatAddActivity;
import com.example.z1229.activity.SportActivity;
import com.example.z1229.activity.SportAddActivity;
import com.example.z1229.activity.SportHistoryActivity;
import com.example.z1229.adapter.XAxisValueFormatter;
import com.example.z1229.base.LineMarkerView;
import com.example.z1229.summerclient.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MoreFragment extends Fragment implements View.OnClickListener {

    private ImageButton clock, eat, sport, blood,eat_p,sport_p,blood_p;
    private RelativeLayout r_eat, r_sport, r_blood;
    private View view;
    private LineChart lineChart;
    private BarChart eat_b,sport_b;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);
        initview();
        initLineChart();
        initLinedata();
        initBarChart(eat_b);
        initBarChart(sport_b);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_clock:
                startActivity(new Intent(AlarmClock.ACTION_SHOW_ALARMS));
                break;
            case R.id.btn_blood_plus:
                startActivity(new Intent(getActivity(),BloodAddActivity.class));
                break;
            case R.id.item_blood:
                startActivity(new Intent(getActivity(),BloodActivity.class));
                break;
            case R.id.btn_eat_plus:
                startActivity(new Intent(getActivity(),EatAddActivity.class));
                break;
            case R.id.item_eat:
                startActivity(new Intent(getActivity(),EatActivity.class));
                break;
            case R.id.item_sport:
                startActivity(new Intent(getActivity(),SportActivity.class));
                break;
            case R.id.btn_sport_plus:
                startActivity(new Intent(getActivity(),SportAddActivity.class));
                break;
            case R.id.more_blood_past:
                startActivity(new Intent(getActivity(),BloodActivity.class));
                break;
            case R.id.more_eat_past:
                startActivity(new Intent(getActivity(),SportHistoryActivity.class));
                break;
            case R.id.more_sport_past:
                startActivity(new Intent(getActivity(),SportHistoryActivity.class));
                break;
            default:
                break;
        }
    }

    private void initview() {
        clock = (ImageButton) view.findViewById(R.id.more_clock);
        eat = (ImageButton) view.findViewById(R.id.btn_eat_plus);
        blood = (ImageButton) view.findViewById(R.id.btn_blood_plus);
        sport = (ImageButton) view.findViewById(R.id.btn_sport_plus);
        r_blood = (RelativeLayout) view.findViewById(R.id.item_blood);
        r_eat = (RelativeLayout) view.findViewById(R.id.item_eat);
        r_sport = (RelativeLayout) view.findViewById(R.id.item_sport);
        eat_b = (BarChart)view.findViewById(R.id.more_eat_bar);
        sport_b=(BarChart)view.findViewById(R.id.more_sport_bar);
        blood_p=(ImageButton)view.findViewById(R.id.more_blood_past);
        eat_p=(ImageButton)view.findViewById(R.id.more_eat_past);
        sport_p=(ImageButton)view.findViewById(R.id.more_sport_past);
        blood_p.setOnClickListener(this);
        sport_p.setOnClickListener(this);
        eat_p.setOnClickListener(this);
        clock.setOnClickListener(this);
        eat.setOnClickListener(this);
        blood.setOnClickListener(this);
        sport.setOnClickListener(this);
        r_sport.setOnClickListener(this);
        r_eat.setOnClickListener(this);
        r_blood.setOnClickListener(this);
    }
    private void initLineChart(){
        lineChart = (LineChart)view.findViewById(R.id.blood_line);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                LineMarkerView lineMarkerView = new LineMarkerView(getActivity(),lineChart.getXAxis().getValueFormatter(),"mmol/L");
                lineMarkerView.setChartView(lineChart);
                lineChart.setMarker(lineMarkerView);
                lineChart.invalidate();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText("暂无数据");
        lineChart.setScaleEnabled(false);
        lineChart.getLegend().setEnabled(false);

        XAxis x = lineChart.getXAxis();
        x.setDrawGridLines(false);
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

    private void initLinedata(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("早餐前");
        arrayList.add("早餐后");
        arrayList.add("午餐前");
        arrayList.add("午餐后");
        arrayList.add("晚餐前");
        arrayList.add("晚餐后");
        arrayList.add("睡觉前");
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

        LineDataSet lineDataSet = new LineDataSet(data,"血糖");
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

    private void initBarChart(final BarChart mChart){
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                LineMarkerView lineMarkerView = new LineMarkerView(getActivity(),mChart.getXAxis().getValueFormatter(),"千卡");
                lineMarkerView.setChartView(mChart);
                mChart.setMarker(lineMarkerView);
                mChart.invalidate();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setDrawGridLines(false);

        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);

        mChart.getLegend().setEnabled(false);

        initBardata(mChart);
    }

    private void initBardata(BarChart mChart){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("7/28");
        arrayList.add("7/29");
        arrayList.add("7/30");
        arrayList.add("7/31");
        arrayList.add("8/1");
        arrayList.add("8/2");
        arrayList.add("8/3");
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

        BarDataSet barDataSet = new BarDataSet(data,"柱形图");
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
