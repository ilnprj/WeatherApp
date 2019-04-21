package com.example.weartherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsModule extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    TextView textGps;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textGps = findViewById(R.id.gpsCoords);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        textGps = findViewById(R.id.gpsCoords);
        textGps.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    private void GetGeoCoderData(View v) {
        Log.d("LocConnection", "Test");
        Geocoder gcd = new Geocoder(v.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            //TODO: Сюда вписать полученные координаты с GPS
            addresses = gcd.getFromLocation(52, 82, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0)
            Log.d("LocConnection", (addresses.get(0).getLocality()) + " N = " + addresses.size());
    }
}
