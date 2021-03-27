package lk.kdu.pulze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ExtendedFloatingActionButton floatingActionButton;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private Button testBtn;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.extended_fab);
        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer);
        testBtn = findViewById(R.id.testBtn);
        navigationView = findViewById(R.id.nav_main);
        lineChart = findViewById(R.id.lineChart);

//        lineChart.setOnChartGestureListener(MainActivity.this);
//        lineChart.setOnChartValueSelectedListener(MainActivity.this);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.getDescription().setEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0,60f));
        yValues.add(new Entry(1,56f));
        yValues.add(new Entry(2,34f));
        yValues.add(new Entry(3,65f));

        LineDataSet set1 = new LineDataSet(yValues,"Data Set One");

        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        set1.setColor(Color.DKGRAY);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        testBtn.setText(sharedPreferences.getString("Name",null));

        //Set Toolbar as ActionBar
//        setSupportActionBar(toolbar);

        //Setup Navigation Drawer
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //Pass context to side-nav item click listener.
        navigationView.setNavigationItemSelectedListener(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
//                Intent settings = new Intent(MainActivity.this,AddDataActivity.class);
//                startActivity(settings);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.getItemId() == R.id.item1) {
            Toast.makeText(getApplicationContext(), "Clicked Item 1", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.item3) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        //close navigation drawer
        drawerLayout.closeDrawers();
        return true;
    }
}