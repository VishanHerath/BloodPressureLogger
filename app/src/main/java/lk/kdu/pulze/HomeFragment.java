package lk.kdu.pulze;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnChartValueSelectedListener {

    private Button testBtn;
    private SharedPreferences sharedPreferences;
    private BarChart barChart;
    private LineChart lineChart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        testBtn = v.findViewById(R.id.testBtn);

        lineChart = v.findViewById(R.id.activity_main_lineChart);

        barChart = v.findViewById(R.id.barChart);

        showLineChart();
        addEntry(12,0); //Index 0 is Systole
        addEntry(5,0); //Index 0 is Systole
        addEntry(65,1); //Index 1 is Diastole
        addEntry(45,1); //Index 1 is Diastole
//        addEntry(65,2); //Index 2 is Pulse

        showBarChart();
        initBarChart();

        //Get SharedPreferences of the user.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        testBtn.setText(sharedPreferences.getString("name", null));
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
                if(setIndex == 1){
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
        set.setColor(Color.GREEN);
        set.setCircleColor(Color.DKGRAY);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
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
        set.setColor(Color.BLUE);
        set.setCircleColor(Color.DKGRAY);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.BLUE);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.GRAY);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    private void showBarChart() {
        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Double> valueList1 = new ArrayList<>();
        ArrayList<BarEntry> entries1 = new ArrayList<>();

        //input data
        for (int i = 0; i < 6; i++) {
            valueList.add(i * 100.1);
            valueList1.add(i * 50.1);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            BarEntry barEntry1 = new BarEntry(i, valueList1.get(i).floatValue());
            entries.add(barEntry);
            entries1.add(barEntry1);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Systole");
        BarDataSet barDataSet1 = new BarDataSet(entries1, "Diastole");
        //Initialize DataSet
        initBarDataSet(barDataSet, "#0288D1");
        initBarDataSet(barDataSet1, "#388E3C");
        BarData data = new BarData(barDataSet, barDataSet1);

        float BAR_SPACE = 0.05f;
        float BAR_WIDTH = 0.2f;
        int GROUPS = 3;

        barChart.setData(data);
        barChart.getBarData().setBarWidth(BAR_WIDTH);

        float groupSpace = 1f - ((BAR_SPACE + BAR_WIDTH) * GROUPS);
        barChart.groupBars(0, groupSpace, BAR_SPACE);
        barChart.invalidate();
    }

    private void initBarDataSet(BarDataSet barDataSet, String color) {
        //Changing the color of the bar
        barDataSet.setColor(Color.parseColor(color));
        //Setting the size of the form in the legend
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);
        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(12f);

        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
    }

    private void initBarChart() {
        String[] months = new String[]{"TYPE 1", "TYPE 2", "TYPE 3", "TYPE 4"};
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(true);

        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000);

        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        xAxis.setCenterAxisLabels(true);
        barChart.getXAxis().setAxisMinimum(0);

//        //Set Axis Labels
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
//        xAxis.setCenterAxisLabels(true);


        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);
        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
