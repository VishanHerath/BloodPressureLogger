package com.example.pulze;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread splash = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(SPLASH_TIME_OUT);
                    // After 5 seconds redirect to another intent
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    //Remove activity
                    finish();
                } catch (Exception e) {
                    //..
                }
            }
        };
        // start thread
        splash.start();
    }
}

