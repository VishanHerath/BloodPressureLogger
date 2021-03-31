package lk.kdu.pulze;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import lk.kdu.pulze.adapter.PressureListAdapter;
import lk.kdu.pulze.helper.DatabaseHelper;
import lk.kdu.pulze.model.PressureModel;

public class ViewPressureList extends AppCompatActivity {

    ListView pressuresListView;
    DatabaseHelper databaseHelper;
    private ArrayList<PressureModel> pressureModelArrayList;
    private PressureListAdapter customAdapter;
    private CoordinatorLayout listCoordinator;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pressure_layout);

        listCoordinator = findViewById(R.id.list_coordinator);

        pressuresListView = findViewById(R.id.pressuresListView);

        lineChart = findViewById(R.id.activity_main_lineChart);

        databaseHelper = new DatabaseHelper(this);

        pressureModelArrayList = databaseHelper.getPressure();

        customAdapter = new PressureListAdapter(this, pressureModelArrayList);
        pressuresListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();


        pressuresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(listCoordinator, String.valueOf(position), Snackbar.LENGTH_LONG).show();
            }
        });

        pressuresListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg, View v, int pos, long id) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ViewPressureList.this);
                builder.setTitle("Delete Record");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PressureModel ids = (PressureModel) pressureModelArrayList.get(pos);
                        databaseHelper.deletePressure(ids.getId());
                        pressureModelArrayList.clear();
                        pressureModelArrayList.addAll(databaseHelper.getPressure());
                        customAdapter.notifyDataSetChanged();
                        pressuresListView.invalidateViews();
                        pressuresListView.refreshDrawableState();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //..
                    }
                });
                builder.setIcon(R.drawable.ic_baseline_info_24);
                builder.setMessage("Are you sure you wanna delete?");
                builder.show();
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewPressureList.this, MainActivity.class));
        finish();
    }
}