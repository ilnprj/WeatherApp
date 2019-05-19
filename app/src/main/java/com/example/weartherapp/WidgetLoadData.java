package com.example.weartherapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import org.json.JSONObject;
import java.util.Date;
import java.util.Locale;

public class WidgetLoadData extends Fragment {

    TextView cityField;
    TextView temperatureField;
    ImageView iconWeather;
    TextView description;
    String lang;
    Handler handler;


    private void updateWeatherData(){
        final String city = new UserPrefs(getActivity()).GetDefaultCity();
        lang = new UserPrefs(getActivity()).GetLang();
        new Thread(){
            public void run(){
                final JSONObject json = DownloadHandler.getJSON(getActivity(), city,lang);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                            iconWeather.setImageResource(R.drawable.sad);
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.getDefault()) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject main = json.getJSONObject("main");
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            temperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");


            description.setText(
                    details.getString("description").toUpperCase()
            );

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            iconWeather.setImageResource(R.drawable.sad);
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset)
    {
        int id = actualId / 100;

        if (actualId ==  800)
        {
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                iconWeather.setImageResource(R.drawable.sun);
            } else {
                iconWeather.setImageResource(R.drawable.night);
            }
        }
        else
        {
            switch (id)
            {
                case 2 : iconWeather.setImageResource(R.drawable.thunder);
                    break;
                case 3 : iconWeather.setImageResource(R.drawable.rain);
                    break;
                case 8 : iconWeather.setImageResource(R.drawable.cloudy);
                    break;
                case 6 : iconWeather.setImageResource(R.drawable.snow);
                    break;
                case 5 : iconWeather.setImageResource(R.drawable.rain);
                    break;
            }
        }
    }
}
