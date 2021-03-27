package lk.kdu.pulze;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Button button;
    private TabLayout tabLayout;
    private SliderPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_activity);

        // making activity full screen
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // hide action bar you can use NoAction theme as well
        //getSupportActionBar().hide();
        // bind views
        viewPager = findViewById(R.id.pagerIntroSlider);
        tabLayout = findViewById(R.id.tabs);
        button = findViewById(R.id.sliderButton);

        // init slider pager adapter
        adapter = new SliderPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // set adapter
        viewPager.setAdapter(adapter);

        // set dot indicators
        tabLayout.setupWithViewPager(viewPager);

        // make status bar transparent
        changeStatusBarColor();

        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (viewPager.getCurrentItem() < adapter.getCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                if(adapter.getCount() == 4 && button.getText() == "Get Started"){
                    startActivity(new Intent(ViewPagerActivity.this, GetDetailsActivity.class));
                }
            }
        });

        /**
         * Add a listener that will be invoked whenever the page changes
         * or is incrementally scrolled
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                if (position == adapter.getCount() - 1) {
                    button.setText("Get Started");
                } else {
                    button.setText("Next");
                }

            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
