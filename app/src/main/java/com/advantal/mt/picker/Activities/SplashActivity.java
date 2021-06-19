package com.advantal.mt.picker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.mt.picker.R;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivSplashImg;
    private Animation customAimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById();

        handleIntent();
    }

    private void handleIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, FilePickerActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void findViewById() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        ivSplashImg = (ImageView) findViewById(R.id.ivSplash);
        customAimation = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        ivSplashImg.setVisibility(View.VISIBLE);
        ivSplashImg.startAnimation(customAimation);
    }
}
