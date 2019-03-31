package com.example.weartherapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment  extends Fragment  {

    TextView cityField;
    TextView temperatureField;
    TextView lastUpdated;
    TextView description ;
    TextView windText;
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
        lastUpdated = rootView.findViewById(R.id.infoText);
        description  = rootView.findViewById(R.id.description );
        windText = rootView.findViewById(R.id.windText);
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWeatherData( new UserPrefs(getActivity()).GetDefaultCity());
    }

    private void updateWeatherData(final String city){

        new Thread(){
            public void run(){
                final JSONObject json = DownloadHandler.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
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
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject main = json.getJSONObject("main");
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            temperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));

            lastUpdated.setText("Last update: " + updatedOn);

            description.setText(
                    details.getString("description").toUpperCase()
            );

            windText.setText(
                    "Wind Speed = "+json.getJSONObject("wind").getString("speed")+"m/s"
            );
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}