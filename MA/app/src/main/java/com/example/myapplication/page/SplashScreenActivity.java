package com.example.myapplication.page;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import com.example.myapplication.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }


}