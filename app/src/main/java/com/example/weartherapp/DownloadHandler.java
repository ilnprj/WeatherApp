package com.example.weartherapp;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import android.content.Context;

public class DownloadHandler {

    private static final String OpenWeatherAPI = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru";//"http://api.openweathermap.org/data/2.5/weather?lat=-16.92&lon=145.77&units=metric&lang=ru"; //q=%s&units=metric";
    public static JSONObject getJSON(Context context, String city){
        try{
            URL url = new URL(String.format(OpenWeatherAPI,city));
            //URL url = new URL(OpenWeatherAPI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(( new InputStreamReader(connection.getInputStream())));
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(52, 82, 1);

            if (addresses.size() > 0)
                Log.d("LocConnection",(addresses.get(0).getLocality())+" N = "+addresses.size());

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while((tmp = reader.readLine())!=null)
            {
                json.append(tmp).append("\n");
            }

            reader.close();
            JSONObject data = new JSONObject(json.toString());

            Log.d("LocConnection",data.get("cod").toString());
            if (data.getInt("cod")!=200)
            {
                return null;
            }

            return data;

        } catch (Exception e)
        {
            //Нужна информация о том что произошло
            e.getMessage();
            Log.d("LocConnection",e.getMessage());
            return null;
        }
    }
}
