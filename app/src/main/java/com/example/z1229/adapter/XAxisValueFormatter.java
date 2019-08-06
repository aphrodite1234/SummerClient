package com.example.z1229.adapter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class XAxisValueFormatter implements IAxisValueFormatter {

    private ArrayList<String> arrayList;

    public XAxisValueFormatter(ArrayList<String> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int position = (int) value;
        if (position >= arrayList.size()) {
            position = 0;
        }
        return arrayList.get(position);
    }
}
