package com.example.weartherapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsModule extends AppCompatActivity implements View.OnClickListener  {

    Button gpsButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        gpsButton = findViewById(R.id.gpsButton);
        gpsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Log.d("LocConnection","Test");
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(52, 82, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0)
            Log.d("LocConnection",(addresses.get(0).getLocality())+" N = "+addresses.size());
    }

}
