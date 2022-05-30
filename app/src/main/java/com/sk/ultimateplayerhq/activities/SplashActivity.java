package com.sk.ultimateplayerhq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {


    private static final long SPLASH_TIME = 3000;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SessionManager.isLogged() ? SessionManager.isPlayerLogin() ? PlayerDashboardActivity.class : MainActivity.class : WelcomeActivity.class));
                finish();
            }
        };
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, SPLASH_TIME);
    }
}
