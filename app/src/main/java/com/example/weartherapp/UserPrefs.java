package com.example.weartherapp;
import android.app.Activity;
import android.content.SharedPreferences;

public class UserPrefs {

    SharedPreferences userData;

    public UserPrefs(Activity activity){
        userData = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String GetDefaultCity()
    {
        return userData.getString("city", "Berlin, DE");
    }

    void SetCity(String city)
    {
        userData.edit().putString("city",city).commit();
    }
}
