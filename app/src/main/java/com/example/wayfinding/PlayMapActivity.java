package com.example.wayfinding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

public class PlayMapActivity extends AppCompatActivity {
    private int start, current, target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_map);
    }
}