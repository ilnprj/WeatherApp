package com.example.weartherapp;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

import android.content.Context;

public class DownloadHandler {

    private static String OpenWeatherAPI = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=";
    public static JSONObject getJSON(Context context, String city, String lang){

        String FullUrl = OpenWeatherAPI+lang;
        try{
            URL url = new URL(String.format(FullUrl,city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(( new InputStreamReader(connection.getInputStream())));
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
