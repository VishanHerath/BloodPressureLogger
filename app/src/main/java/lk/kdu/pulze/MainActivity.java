package lk.kdu.pulze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    private ExtendedFloatingActionButton floatingActionButton;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NestedScrollView scrollView;
    private NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.extended_fab);
        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_main);
        scrollView = findViewById(R.id.mainScrollView);


        setSupportActionBar(toolbar);

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
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        //Pass context to side-nav item click listener.
        navigationView.setNavigationItemSelectedListener(this);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    floatingActionButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close));
                    floatingActionButton.hide();
                }

                if (scrollY == 0) {
                    floatingActionButton.show();
                    floatingActionButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open));
                }

            }
        });

        //Load Default Fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.getItemId() == R.id.item1) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
            fragmentTransaction.commit();
            toolbar.setTitle("Home");
            floatingActionButton.show();
            floatingActionButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open));

        }
        if (item.getItemId() == R.id.item2) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new StatisticsFragment());
            fragmentTransaction.commit();
            toolbar.setTitle("Statistics");
            floatingActionButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close));
            floatingActionButton.hide();
        }
        if (item.getItemId() == R.id.item3) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        //close navigation drawer
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}