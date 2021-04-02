package lk.kdu.pulze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

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
    private Chip chipAll, chipStat;
    private ListView homeListView;
    private DatabaseHelper databaseHelper;
    private ArrayList<PressureModel> pressureModelArrayList;
    private PressureListAdapter customAdapter;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private ExtendedFloatingActionButton floatingActionButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = requireActivity().findViewById(R.id.topAppBar);
        floatingActionButton = requireActivity().findViewById(R.id.extended_fab);
        navigationView = requireActivity().findViewById(R.id.nav_main);

        lineChart = v.findViewById(R.id.activity_main_lineChart);
        chipAll = v.findViewById(R.id.chip_all);
        chipStat = v.findViewById(R.id.chip_stat);

        lineChartHelper = new LineChartHelper(lineChart);

        lineChartHelper.showLineChart();

        homeListView = v.findViewById(R.id.home_list);

        databaseHelper = new DatabaseHelper(getContext());

        pressureModelArrayList = databaseHelper.getPressure();

        for (PressureModel model : pressureModelArrayList) {
            lineChartHelper.addEntry(model.getSystolic(), 0); //Index 0 is Systolic
            lineChartHelper.addEntry(model.getDiastolic(), 1); //Index 1 is Diastolic
        }

        Collections.reverse(pressureModelArrayList);
        customAdapter = new PressureListAdapter(getContext(), pressureModelArrayList);
        homeListView.setAdapter(customAdapter);


        toolbar.setTitle("Home");
        floatingActionButton.show();

//        addEntry(65,2); //Index 2 is Pulse

//        Get SharedPreferences of the user.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        Toast.makeText(getContext(), "Welcome Back " + sharedPreferences.getString("name", null), Toast.LENGTH_LONG).show();


        chipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getActivity(), ViewPressureList.class));
            }
        });

        chipStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(1).setChecked(true);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new StatisticsFragment())
                        .commit();
            }
        });

        return v;
    }


}
