package com.example.weartherapp;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsModule extends AppCompatActivity   {

    private String exampleText;

    //Пример простого конструктора
    public GpsModule() {
        exampleText = "Constructor Inited";
    }

    public View.OnClickListener example = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            GetGeoCoderData(v);
        }
    };

    private void GetGeoCoderData(View v)
    {
        exampleText = "GeoCoderMethod";
        Log.d("LocConnection","Test");
        Geocoder gcd = new Geocoder(v.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            //TODO: Сюда вписать полученные координаты с GPS
            addresses = gcd.getFromLocation(52, 82, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0)
            Log.d("LocConnection",(addresses.get(0).getLocality())+" N = "+addresses.size());
    }

    public String GetTest()
    {
        return exampleText;
    }

}
