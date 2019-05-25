package com.example.weartherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            SetSettingsClick(remoteViews, context, appWidgetManager, appWidgetId);
            setInfo(context);
            Log.d("LocConnection","Update");
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void SetSettingsClick(RemoteViews remoteViews, Context context, AppWidgetManager appWidgetManager, int appWidgetIds) {
        Intent configIntent = new Intent(context, Settings.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widgetViewLogo, configPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        super.onReceive(context, intent);
        setInfo(context);
    }

    private void setInfo(Context context)
    {
        AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        ComponentName watchWidget = new ComponentName(context, NewAppWidget.class);

        Toast.makeText(context, "Requested", Toast.LENGTH_SHORT).show();

        // Check the internet connection availability
        if(InternetConnected.isInternetConnected()){
            Toast.makeText(context, "Fetching Data", Toast.LENGTH_SHORT).show();
            // Update the widget weather data
            // Execute the AsyncTask
            new WidgetLoadData(appWidgetManager,watchWidget,remoteViews,context);

        }else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }
        // Apply the changes
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }
}

