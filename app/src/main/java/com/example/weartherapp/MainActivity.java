package com.example.weartherapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenSettings = findViewById(R.id.buttonSettings);
        btnOpenSettings.setOnClickListener(this);

        updateWeatherData("Berlin");
    }

    //Один метод для всех кнопок. Внутри метода идет проверка по ID кнопки. Вызываем необходимые активити.
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.buttonSettings:
            {
                intent = new Intent(this, Settings.class);
                break;
            }
            default:
                {
                    intent = new Intent(this, Settings.class);
                    break;
                }
        }
        startActivity(intent);
    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = DownloadHandler.getJSON(getApplicationContext(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(),
                                    getApplicationContext().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                           // renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
}
