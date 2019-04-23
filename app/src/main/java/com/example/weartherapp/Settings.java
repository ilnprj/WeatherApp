package com.example.weartherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_CODE_PERMISSION = 0;
    TextView text;
    Button backBtn;
    Button gpsTestBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Задействуем первоначальную реализацию родителя
        super.onCreate(savedInstanceState);
        //Задаем интерфейс в Activity
        setContentView(R.layout.activity_settings);
        //Инициализация содержимого в интерфейсе
        setButtons();
        setTextViews();
    }

    private void setButtons()
    {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        gpsTestBtn = findViewById(R.id.gpsButton);
        gpsTestBtn.setOnClickListener(this);
    }

    private void setTextViews()
    {
        text = findViewById(R.id.editText2);
        text.setText(new UserPrefs(this).GetDefaultCity());
        text.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdateCity(text.getText().toString());
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.backBtn: {
                onBackPressed();
                break;
            }

            case R.id.gpsButton: {

                int permissionStatusGps = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                int permissionsStatusInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                if ((permissionStatusGps != PackageManager.PERMISSION_GRANTED) || (permissionsStatusInternet !=  PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_PERMISSION);
                    return;
                }
                Intent item = new Intent(this,gpsClass.class);
                startActivity(item);
                break;
            }
        }
    }

    private void UpdateCity(String inputCity)
    {
        new UserPrefs(this).SetCity(inputCity);
    }
}
