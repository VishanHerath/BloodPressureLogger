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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Button testBtn;
    private SharedPreferences sharedPreferences;
    private LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        testBtn = v.findViewById(R.id.testBtn);
        lineChart = v.findViewById(R.id.lineChart);

        //        lineChart.setOnChartGestureListener(MainActivity.this);
//        lineChart.setOnChartValueSelectedListener(MainActivity.this);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.getDescription().setEnabled(false);



        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 56f));
        yValues.add(new Entry(2, 34f));
        yValues.add(new Entry(3, 65f));

        LineDataSet set1 = new LineDataSet(yValues, "Data Set One");

        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        set1.setColor(Color.DKGRAY);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        testBtn.setText(sharedPreferences.getString("name", null));
        return v;
    }
}
