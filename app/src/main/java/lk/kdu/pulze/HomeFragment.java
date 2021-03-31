package lk.kdu.pulze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Collections;

import lk.kdu.pulze.adapter.PressureListAdapter;
import lk.kdu.pulze.helper.DatabaseHelper;
import lk.kdu.pulze.helper.LineChartHelper;
import lk.kdu.pulze.model.PressureModel;

public class HomeFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private LineChart lineChart;
    private LineChartHelper lineChartHelper;
    private Chip chipAll;
    ListView homeListView;
    DatabaseHelper databaseHelper;
    private ArrayList<PressureModel> pressureModelArrayList;
    private PressureListAdapter customAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        lineChart = v.findViewById(R.id.activity_main_lineChart);
        chipAll = v.findViewById(R.id.chip_all);

        lineChartHelper = new LineChartHelper(lineChart);

        lineChartHelper.showLineChart();

        lineChartHelper.addEntry(12, 0); //Index 0 is Systole
        lineChartHelper.addEntry(5, 0); //Index 0 is Systole
        lineChartHelper.addEntry(65, 1); //Index 1 is Diastole
        lineChartHelper.addEntry(45, 1); //Index 1 is Diastole

        homeListView = v.findViewById(R.id.home_list);

        databaseHelper = new DatabaseHelper(getContext());

        pressureModelArrayList = databaseHelper.getPressure();

        customAdapter = new PressureListAdapter(getContext(), pressureModelArrayList);
        homeListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();


//        addEntry(65,2); //Index 2 is Pulse

//        Get SharedPreferences of the user.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Toast.makeText(getContext(), "Welcome Back " + sharedPreferences.getString("name", null), Toast.LENGTH_LONG).show();


        chipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewPressureList.class));
            }
        });
        return v;
    }

    private String getDateProperty(PressureModel item) {
        return item.getDatetime();
    }
}
