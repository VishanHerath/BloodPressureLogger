package lk.kdu.pulze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnChartValueSelectedListener {

    private SharedPreferences sharedPreferences;
    private LineChart lineChart;
    private Chip chipAll;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        lineChart = v.findViewById(R.id.activity_main_lineChart);
        chipAll = v.findViewById(R.id.chip_all);

        showLineChart();
        addEntry(12, 0); //Index 0 is Systole
        addEntry(5, 0); //Index 0 is Systole
        addEntry(65, 1); //Index 1 is Diastole
        addEntry(45, 1); //Index 1 is Diastole
//        addEntry(65,2); //Index 2 is Pulse

        //Get SharedPreferences of the user.
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        testBtn.setText(sharedPreferences.getString("name", null));


        chipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewPressureList.class));
            }
        });
        return v;
    }

    private void showLineChart() {
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

        lineChart.getDescription().setText(getResources().getString(R.string.app_name));
        lineChart.getDescription().setTextColor(Color.GRAY);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.GRAY);

        XAxis xl = lineChart.getXAxis();
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(Color.GRAY);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.GRAY);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void addEntry(float value, int setIndex) {

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

    private LineDataSet createSystoleSet() {
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

    private LineDataSet createDiastoleSet() {
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
