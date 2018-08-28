package com.example.app.calculator;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.app.calculator.databinding.ActivityBrowserBinding;
import com.util.Website;

public class BrowserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBrowserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_browser);

        final ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setHomeAsUpIndicator(null);
        }

        binding.aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Website aboutTheDev = new Website(getApplicationContext(), BrowserActivity.this,
                        "https://alanluu.github.io", true);
                aboutTheDev.build();
            }
        });

        binding.circleCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Website circleCanvas = new Website(getApplicationContext(), BrowserActivity.this,
                        "https://alanluu.github.io/circle-canvas", false);
                circleCanvas.build();
            }
        });

        binding.github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Website githubRepo = new Website(getApplicationContext(), BrowserActivity.this,
                        "https://github.com/AlanLuu/android-calculator", false);
                githubRepo.build();
            }
        });

        binding.gravity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionbar != null) actionbar.setTitle("Gravity simulator");
                GravitySim g = new GravitySim(getApplicationContext());
                setContentView(g);
                new Thread(g).start();
            }
        });

        binding.circles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionbar != null) actionbar.setTitle("Random circles");
                RandomCircles r = new RandomCircles(getApplicationContext());
                setContentView(r);
                new Thread(r).start();
            }
        });
    }
}