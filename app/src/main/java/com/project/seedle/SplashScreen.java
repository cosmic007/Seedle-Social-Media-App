package com.project.seedle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.seedle.Activities.LoginPage;

public class SplashScreen extends AppCompatActivity {

    private ImageView Logo;

    private TextView Tag;

    private static final int SPLASH_TIMEOUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Logo = findViewById(R.id.logo);
        Tag = findViewById(R.id.tagline);
        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 100, 0);
        translateAnimation.setDuration(2500);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2500);

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);

        Logo.startAnimation(animationSet);


        // Delay the start of the LoginActivity for a few seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}