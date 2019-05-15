package com.example.weartherapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnOpenSettings;
    Button btnShowWeather;
    ImageView logo;
    View mainScreen;
    TextView versionApp;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = new UserPrefs(this).GetLang();
        SetLocale(lang);
        setContentView(R.layout.activity_main);
        setViewComponents();
        setTouchListener();
    }

    //Один метод для всех кнопок. Внутри метода идет проверка по ID кнопки. Вызываем необходимые активити.
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonSettings: {
                intent = new Intent(this, Settings.class);
                break;
            }

            case R.id.buttonShowWeather: {
                intent = new Intent(this, Weather.class);
                break;
            }
            default: {
                intent = new Intent(this, Settings.class);
                break;
            }
        }
        startActivity(intent);
    }

    private void setViewComponents()
    {
        btnOpenSettings = findViewById(R.id.buttonSettings);
        btnShowWeather = findViewById(R.id.buttonShowWeather);
        mainScreen = findViewById(R.id.viewMain);
        versionApp = findViewById(R.id.versionApp);
        versionApp.setText("Version "+BuildConfig.VERSION_NAME);
        btnShowWeather.setOnClickListener(this);
        btnOpenSettings.setOnClickListener(this);
        logo = findViewById(R.id.logoApp);
    }
    
    private void setTouchListener()
    {
        mainScreen.setOnTouchListener(new OnSwipeListener(MainActivity.this)
        {
            @Override
            public void onSwipeLeft() {
                Intent i =  new Intent(MainActivity.this, Settings.class);
                startActivity(i);
            }

            @Override
            public void onSwipeRight() {
                Intent i = new Intent(MainActivity.this, Weather.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeonce));
        btnOpenSettings.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeonce));
        btnShowWeather.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeonce));
    }

    private void SetLocale(String inputLang)
    {
        Locale locale = new Locale(inputLang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            getBaseContext().getResources()
                    .updateConfiguration(configuration,
                            getBaseContext()
                                    .getResources()
                                    .getDisplayMetrics());
        }
    }
}
