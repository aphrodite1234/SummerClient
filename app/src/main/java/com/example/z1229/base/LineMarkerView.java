package com.example.z1229.base;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.z1229.summerclient.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class LineMarkerView extends MarkerView {

    private LinearLayout linearLayout;
    private TextView textView;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat(".0");
    private String k;


    public LineMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter,String k) {
        super(context, R.layout.blood_line_mark);
        this.xAxisValueFormatter = xAxisValueFormatter;
        this.k = k;
        textView = (TextView)findViewById(R.id.blood_mark);
        linearLayout = (LinearLayout)findViewById(R.id.blood_mark_lin);
    }

    public void setColor(int color){
        linearLayout.setBackgroundColor(color);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        textView.setText(xAxisValueFormatter.getFormattedValue(e.getX(),null)+" "+df.format(e.getY())+k);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return super.getOffset();
    }
}
