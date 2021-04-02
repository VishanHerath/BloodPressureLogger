package lk.kdu.pulze.helper;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import lk.kdu.pulze.model.PressureModel;

public class LineChartHelper implements OnChartValueSelectedListener {

    private LineChart lineChart;
    private  ArrayList<PressureModel> pressureModels;

    public LineChartHelper(LineChart lineChart, ArrayList<PressureModel> models) {
        this.lineChart = lineChart;
        this.pressureModels = models;
    }

    public void showLineChart() {
        lineChart.setOnChartValueSelectedListener(this);                // enable description text
        lineChart.getDescription().setEnabled(true);

        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.setDrawBorders(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        lineChart.animateY(1000, Easing.Linear);
        //setting animation for x-axis, the bar will pop up separately within the time we set
//        lineChart.animateX(1000);

        // set an alternative background color
//        lineChart.setBackgroundColor(getResources().getColor(R.color.white));

        LineData data = new LineData();
        data.setValueTextColor(Color.GRAY);

        // add empty data
        lineChart.setData(data);

        lineChart.getDescription().setText("Pulze");
        lineChart.getDescription().setTextColor(Color.GRAY);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        //Increase Legend Gap
        lineChart.setExtraOffsets(0f,0f,0f,15f);
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.GRAY);

        XAxis xl = lineChart.getXAxis();
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(Color.GRAY);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setGranularity(1);



//        ValueFormatter valueFormatter = new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                if (value >= 0) {
//                    return pressureModels.get((int)value).getDatetime();
//                } else {
//                    return "";
//                }
//            }
//        };

//        lineChart.getXAxis().setValueFormatter(valueFormatter);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setAxisMaximum(500f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.GRAY);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void addEntry(float value, int setIndex) {

        LineData data = lineChart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(setIndex);
            if (set == null) {
                if (setIndex == 0) {
                    set = createSystoleSet();
                }
                if (setIndex == 1) {
                    set = createDiastoleSet();
                }
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), value), setIndex);
            data.notifyDataChanged();

            // let the graph know it's data has changed
            lineChart.notifyDataSetChanged();

            // limit the number of visible entries
            lineChart.setVisibleXRangeMaximum(120);

            // move to the latest entry
            lineChart.moveViewToX(data.getEntryCount());
        }
    }

    public LineDataSet createSystoleSet() {
        LineDataSet set = new LineDataSet(null, "Systole");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor("#311B92"));
        set.setCircleColor(Color.DKGRAY);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(25);
        set.setFillColor(Color.BLUE);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.GRAY);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    public LineDataSet createDiastoleSet() {
        LineDataSet set = new LineDataSet(null, "Diastole");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor("#B71C1C"));
        set.setCircleColor(Color.DKGRAY);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(25);
        set.setFillColor(Color.BLUE);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.GRAY);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
