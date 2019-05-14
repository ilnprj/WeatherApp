package com.example.weartherapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnOpenSettings;
    Button btnShowWeather;
    ImageView logo;
    View mainScreen;
    TextView versionApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
