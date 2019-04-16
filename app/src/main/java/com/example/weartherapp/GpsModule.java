package com.example.weartherapp;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsModule extends AppCompatActivity   {

    public static View.OnClickListener example = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d("LocConnection","Test");
            Geocoder gcd = new Geocoder(v.getContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(52, 82, 10);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses.size() > 0)
                Log.d("LocConnection",(addresses.get(0).getLocality())+" N = "+addresses.size());
        }
    };

    public void onClickGps()
    {

    }

}
