package com.example.weartherapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherFragment  extends Fragment  {

    TextView cityField;
    TextView temperatureField;

    Handler handler;

    public WeatherFragment()
    {
        handler = new Handler();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_weather, container, false);
        cityField = rootView.findViewById(R.id.cityText);
        temperatureField = rootView.findViewById(R.id.tempText);
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
