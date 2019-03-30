package com.example.weartherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenSettings = findViewById(R.id.buttonSettings);
        Button btnShowWeather = findViewById(R.id.buttonShowWeather);
        btnShowWeather.setOnClickListener(this);
        btnOpenSettings.setOnClickListener(this);
    }

    //Один метод для всех кнопок. Внутри метода идет проверка по ID кнопки. Вызываем необходимые активити.
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonSettings: {
                intent = new Intent(this, Settings.class);
                break;
            }

            case R.id.buttonShowWeather: {
                intent = new Intent(this, Weather.class);
                break;
            }
            default: {
                intent = new Intent(this, Settings.class);
                break;
            }
        }
        startActivity(intent);
    }
}
