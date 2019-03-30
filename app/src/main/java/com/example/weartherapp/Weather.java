package com.example.weartherapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

public class Weather extends AppCompatActivity implements View.OnClickListener {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Button backBtn = findViewById(R.id.backBtnWeather);
        backBtn.setOnClickListener(this);
        updateWeatherData("Berlin");
    }

    public void onClick(View v)
    {
        onBackPressed();
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
