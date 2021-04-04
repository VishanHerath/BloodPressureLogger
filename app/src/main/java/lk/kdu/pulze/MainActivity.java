package lk.kdu.pulze;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private ExtendedFloatingActionButton floatingActionButton;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NestedScrollView scrollView;
    private NavigationView navigationView;

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
                // the delay of the extension of the FAB is set for 12 items
                if (scrollY > oldScrollY + 12 && floatingActionButton.isShown()) {
                    floatingActionButton.shrink();
                }

                // the delay of the extension of the FAB is set for 12 items
                if (scrollY < oldScrollY - 12 && !floatingActionButton.isShown()) {
                    floatingActionButton.show();
                }

                if (v.getChildAt(0).getBottom()
                        <= (v.getHeight() + v.getScrollY())) {
                    //scroll view is at bottom
                    floatingActionButton.hide();
                }

                // if the nestedScrollView is at the first item of the list then the
                // floating action should be in show state
                if (scrollY == 0) {
                    floatingActionButton.show();
                    floatingActionButton.extend();
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
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        // Handle navigation view item clicks here.

        //Post Delay to open fragment after the drawer is closed.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (item.getItemId() == R.id.item1) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out  // popExit
                            )
                            .replace(R.id.container_fragment, new HomeFragment())
                            .commit();
                    getSupportFragmentManager().popBackStackImmediate(null, 0);

                }
                if (item.getItemId() == R.id.item2) {
                    getSupportFragmentManager().beginTransaction()
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
                if (item.getItemId() == R.id.item3) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                }
            }
        }, 350);


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