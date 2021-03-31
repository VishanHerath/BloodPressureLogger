package lk.kdu.pulze;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pressure_list);

        listCoordinator = findViewById(R.id.list_coordinator);

        pressuresListView = (ListView) findViewById(R.id.pressuresListView);

        databaseHelper = new DatabaseHelper(this);

        pressureModelArrayList = databaseHelper.getPressure();

        customAdapter = new PressureListAdapter(this, pressureModelArrayList);
        pressuresListView.setAdapter(customAdapter);

        pressuresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(listCoordinator,String.valueOf(position),Snackbar.LENGTH_LONG).show();
            }
        });

    }
}