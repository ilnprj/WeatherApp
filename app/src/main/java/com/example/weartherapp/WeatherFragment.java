package com.example.weartherapp;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
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
    ImageView iconWeather;
    ListView swipeWeather;
    Handler handler;
    float currentTemperature;

    public WeatherFragment()
    {
        handler = new Handler();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_weather, container, false);
        setViewComponents(rootView);
        swipeWeather.setOnTouchListener(new OnSwipeListener(getActivity())
                                        {
                                            public void onSwipeBottom() {
                                                Toast.makeText(getContext(), R.string.tryConnect, Toast.LENGTH_SHORT).show();
                                                updateWeatherData( new UserPrefs(getActivity()).GetDefaultCity());
                                            }
                                        }
        );
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
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject main = json.getJSONObject("main");
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            temperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            currentTemperature = Float.valueOf((float) main.getDouble("temp"));
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));

            lastUpdated.setText("Last update: " + updatedOn);

            description.setText(
                    details.getString("description").toUpperCase()
            );

            windText.setText(
                    "Wind Speed = "+json.getJSONObject("wind").getString("speed")+"m/s"
            );

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

            setAnimations();
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

    private void setViewComponents(View rootView)
    {
        cityField = rootView.findViewById(R.id.cityText);
        temperatureField = rootView.findViewById(R.id.tempText);
        lastUpdated = rootView.findViewById(R.id.infoText);
        description  = rootView.findViewById(R.id.description );
        windText = rootView.findViewById(R.id.windText);
        iconWeather = rootView.findViewById(R.id.iconWeather);
        swipeWeather = rootView.findViewById(R.id.swipeView);
    }

    private void setAnimations()
    {
        setAnimView(cityField,0,R.anim.fadeonce);
        setAnimView(iconWeather,500,R.anim.fadeonce);
        setAnimView(iconWeather,500,R.anim.scale_in);
        setAnimView(temperatureField,1000,R.anim.fadeonce);
        setAnimView(description,1500,R.anim.fadeonce);
        setAnimView(windText,2000,R.anim.fadeonce);
        setAnimView(lastUpdated,2500,R.anim.fadeonce);
        setAnimValue(2500,0);
    }

    private void setAnimView(View v, long delay,int id)
    {
        Animation itemAnim = AnimationUtils.loadAnimation(v.getContext(),id);
        itemAnim.setStartOffset(delay);
        v.setAnimation(itemAnim);
    }

    private void setAnimValue(long duration,long from)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from,currentTemperature);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                temperatureField.setText(String.format("%.2f",valueAnimator.getAnimatedValue())+" ℃");
            }
        });
        valueAnimator.start();
    }

}
