package com.example.myapplication34;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);
        TextView title = findViewById(R.id.appTitle);

        // Fade-in animation for logo
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);
        fadeIn.setStartOffset(500);
        logo.startAnimation(fadeIn);

        // Slide-up animation for title
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideUp.setDuration(1000);
        slideUp.setStartOffset(1000);
        title.startAnimation(slideUp);

        // Delay and navigate to LoginActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}