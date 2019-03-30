package com.example.weartherapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Weather extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Button backBtn = findViewById(R.id.backBtnWeather);
        backBtn.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        onBackPressed();
    }
}
