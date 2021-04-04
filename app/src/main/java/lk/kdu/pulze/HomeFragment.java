package lk.kdu.pulze;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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


        homeListView = v.findViewById(R.id.home_list);

        databaseHelper = new DatabaseHelper(getContext());

        pressureModelArrayList = databaseHelper.getPressure();

        lineChartHelper = new LineChartHelper(lineChart, pressureModelArrayList);

        lineChartHelper.showLineChart();
        for (PressureModel model : pressureModelArrayList) {
            lineChartHelper.addEntry(model.getSystolic(), 0); //Index 0 is Systolic
            lineChartHelper.addEntry(model.getDiastolic(), 1); //Index 1 is Diastolic
        }

        Collections.reverse(pressureModelArrayList);

        ArrayList<PressureModel> homeModel = new ArrayList<>();
        if (!pressureModelArrayList.isEmpty()) {
            if (pressureModelArrayList.size() < 2) {
                homeModel.add(pressureModelArrayList.get(0));
            }
            else{
                homeModel.add(pressureModelArrayList.get(0));
                homeModel.add(pressureModelArrayList.get(1));
            }
            customAdapter = new PressureListAdapter(getContext(), homeModel);
        } else {
            customAdapter = new PressureListAdapter(getContext(), pressureModelArrayList);
        }

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
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .addToBackStack(null)
                        .replace(R.id.container_fragment, new StatisticsFragment())
                        .commit();
            }
        });

        homeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg, View v, int pos, long id) {
                PressureModel item = (PressureModel) pressureModelArrayList.get(pos);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
//                String[] dialogItems = new String[]{
//                        "Systolic Value: " + item.getSystolic(),
//                        "Diastolic Value: " + item.getSystolic()
//                };
//                builder.setItems(dialogItems, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setMessage(
                        "Systole: " + item.getSystolic() + "\n" +
                                "Diastole: " + item.getDiastolic() + "\n" +
                                "Pulse: " + item.getPulse() + "\n" +
                                "Notes: " + item.getNotes());

                builder.setTitle(item.getDatetime());
                builder.show();
                return true;
            }
        });

        return v;
    }


}
