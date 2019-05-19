package com.example.weartherapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.os.Handler;
import org.json.JSONObject;

public class WidgetLoadData {

    private AppWidgetManager appWidgetManager;
    private ComponentName watchWidget;
    private RemoteViews remoteViews;

    String lang;
    Handler handler;

    public WidgetLoadData(AppWidgetManager appWidgetManager, ComponentName watchWidget, RemoteViews remoteViews,Context context)
    {
        this.appWidgetManager = appWidgetManager;
        this.watchWidget = watchWidget;
        this.remoteViews = remoteViews;
        handler = new Handler();
        updateWeatherData(context);
    }

    public void updateWeatherData(final Context context){
        final String city = "Samara";
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
            JSONObject main = json.getJSONObject("main");
            remoteViews.setTextViewText(R.id.appwidget_text,String.format("%.2f", main.getDouble("temp"))+ " ℃" );
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
            Log.d("LocConnection  = ","OK");
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}
