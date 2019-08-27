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
import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.base.LineMarkerView;
import com.example.z1229.summerclient.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private ImageButton clock, eat, sport, blood,eat_p,sport_p,blood_p,load;
    private RelativeLayout r_eat, r_sport, r_blood;
    private View view;
    private LineChart lineChart;
    private BarChart eat_b,sport_b;
    private DBOpenHelper dbOpenHelper;
    private ArrayList<ArrayList<String>> b_line = new ArrayList<>();
    private ArrayList<ArrayList<String>> barData_e = new ArrayList<>();
    private ArrayList<ArrayList<String>> barData_s = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);
        dbOpenHelper = new DBOpenHelper(getActivity());
        b_line=dbOpenHelper.search_b(dbOpenHelper,1);
        barData_e = dbOpenHelper.search_e(dbOpenHelper,7);
        barData_s = dbOpenHelper.search_s(dbOpenHelper,7);
        initview();
        initLineChart();
        initLinedata();
        initBarChart(eat_b,barData_e);
        initBarChart(sport_b,barData_s);
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
                startActivity(new Intent(getActivity(),SportHistoryActivity.class)
                        .putExtra("title","饮食摄入柱形图"));
                break;
            case R.id.more_sport_past:
                startActivity(new Intent(getActivity(),SportHistoryActivity.class)
                        .putExtra("title","运动消耗柱形图"));
                break;
            case R.id.more_load:
                break;
            default:
                break;
        }
    }

    private void initview() {
        clock = (ImageButton) view.findViewById(R.id.more_clock);
        eat = (ImageButton) view.findViewById(R.id.btn_eat_plus);
        load=(ImageButton)view.findViewById(R.id.more_load);
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
        load.setOnClickListener(this);
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
        leftAxis.setAxisMinimum(0f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setLabelCount(7);
    }

    private void initLinedata(){
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Entry> data = new ArrayList<>();
        arrayList.add("早餐前");
        arrayList.add("早餐后");
        arrayList.add("午餐前");
        arrayList.add("午餐后");
        arrayList.add("晚餐前");
        arrayList.add("晚餐后");
        arrayList.add("睡觉前");
        data.add(new Entry(0,0));
        data.add(new Entry(1,0));
        data.add(new Entry(2,0));
        data.add(new Entry(3,0));
        data.add(new Entry(4,0));
        data.add(new Entry(5,0));
        data.add(new Entry(6,0));
        if(b_line.size()>0){
            for (ArrayList<String> item:b_line){
                int po = Integer.valueOf(item.get(0));
                float value = Float.valueOf(item.get(1));
                data.set(po,new Entry(po,value));
            }
        }
        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(arrayList);
        lineChart.getXAxis().setValueFormatter(xAxisValueFormatter);

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
        lineChart.animateY(1000);
    }

    private void initBarChart(final BarChart mChart,ArrayList<ArrayList<String>> data){
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
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setScaleEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);

        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.animateY(1000);

        initBardata(mChart,data);
    }

    private void initBardata(BarChart mChart,ArrayList<ArrayList<String>> dataarray){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<BarEntry> data = new ArrayList<>();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        for (int i=0;i<7;i++){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            arrayList.add(dateFormat.format(calendar.getTime()));
            data.add(new BarEntry(i,0));
        }
        for (ArrayList<String> s:dataarray){
            float value = Float.valueOf(s.get(0));
            String date = s.get(1).substring(5);
            for (int j=0;j<arrayList.size();j++){
                if(date.equals(arrayList.get(j))){
                    data.set(j,new BarEntry(j,value));
                }
            }
        }

        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(arrayList);
        mChart.getXAxis().setValueFormatter(xAxisValueFormatter);
        BarDataSet barDataSet = new BarDataSet(data,"");
        barDataSet.setDrawIcons(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);
        BarData data1 = new BarData(dataSets);
        data1.setValueTextSize(10f);
        data1.setBarWidth(0.5f);

        mChart.setData(data1);
        mChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        b_line=dbOpenHelper.search_b(dbOpenHelper,1);
        initLinedata();
        barData_e = dbOpenHelper.search_e(dbOpenHelper,7);
        initBardata(eat_b,barData_e);
        barData_s = dbOpenHelper.search_s(dbOpenHelper,7);
        initBardata(sport_b,barData_s);
    }
}
