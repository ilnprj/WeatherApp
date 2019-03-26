package com.example.weartherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button btn = findViewById(R.id.backBtn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        onBackPressed();
    }
}
