package com.example.weartherapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.os.Handler;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

public class WidgetLoadData {

    private AppWidgetManager appWidgetManager;
    private ComponentName watchWidget;
    private RemoteViews remoteViews;

    private String lang;
    private Handler handler;

    public WidgetLoadData(AppWidgetManager appWidgetManager, ComponentName watchWidget, RemoteViews remoteViews,Context context)
    {
        this.appWidgetManager = appWidgetManager;
        this.watchWidget = watchWidget;
        this.remoteViews = remoteViews;
        handler = new Handler();
        updateWeatherData(context);
    }

    private void updateWeatherData(final Context context){
        final String city = new UserPrefs(context).GetDefaultCity();
        lang = new UserPrefs(context).GetLang();

        new Thread(){
            public void run(){
                final JSONObject json = DownloadHandler.getJSON(context, city,lang);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            //TODO: Error message here
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
            //Берем из json данные о температуре
            JSONObject main = json.getJSONObject("main");
            remoteViews.setTextViewText(R.id.appwidget_text,String.format("%.2f", main.getDouble("temp"))+ " ℃" );

            //Данные о типе погоды
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            remoteViews.setTextViewText(R.id.typeWeatherWidget,   details.getString("description").toUpperCase());

            //Название города
            remoteViews.setTextViewText(R.id.cityWeatherWidget,json.getString("name").toUpperCase(Locale.getDefault()));

            //Лого погоды
            int idWeather = details.getInt("id");
            long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
            long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;
            remoteViews.setImageViewResource(R.id.widgetViewLogo,GetCurrentIcon(idWeather,sunrise,sunset));

            //Обновляем данные в виджете.
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }catch(Exception e){
            Log.e("LocConnection", "One or more fields not found in the JSON data");
        }
    }

    private int GetCurrentIcon(int actualId,long sunrise,long sunset)
    {
        int id = actualId / 100;
        if (actualId ==  800)
        {
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
               return R.drawable.sun;
            } else {
                return R.drawable.night;
            }
        }
        else
        {
            switch (id)
            {
                case 2 : return R.drawable.thunder;
                case 3 : return R.drawable.rain;
                case 8 : return R.drawable.cloudy;
                case 6 : return R.drawable.snow;
                case 5 : return R.drawable.rain;
                default: return R.drawable.sun;
            }
        }
    }
}
