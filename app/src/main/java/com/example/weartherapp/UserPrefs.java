package com.example.weartherapp;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;

public class UserPrefs {

    SharedPreferences userData;

    public UserPrefs(Context context){
        //Если делать обычный getPrefrences, то информация не передается между разными вызовами Activity
        userData = context.getSharedPreferences("city",0);
    }

    public String GetDefaultCity()
    {
        return userData.getString("city", "Novosibirsk");
    }

    void SetCity(String city)
    {
        userData.edit().putString("city",city).commit();
    }

    void SetLang(String lang) { userData.edit().putString("lang",lang).commit(); }

    public String GetLang()
    {
        return userData.getString("lang", "default");
    }
}
