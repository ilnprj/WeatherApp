package com.example.weartherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_CODE_PERMISSION = 0;
    private SharedPreferences preferences;
    double longitude;
    double latitude;

    TextView headerPage;
    TextView changeCityText;
    TextView nameCity;
    TextView gpsCoords;
    Button backBtn;
    Button gpsTestBtn;
    Button enLocaleBtn;
    Button ruLocaleBtn;

    GPSController gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Задействуем первоначальную реализацию родителя
        super.onCreate(savedInstanceState);
        //Задаем интерфейс в Activity
        setContentView(R.layout.activity_settings);
        //Инициализация содержимого в интерфейсе
        setButtons();
        setTextViews();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextViews();
    }

    private void setButtons()
    {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        gpsTestBtn = findViewById(R.id.gpsButton);
        gpsTestBtn.setOnClickListener(this);
        ruLocaleBtn = findViewById(R.id.ruLocale);
        enLocaleBtn = findViewById(R.id.enLocale);
    }

    private void setTextViews()
    {
        headerPage = findViewById(R.id.headerSettings);
        changeCityText = findViewById(R.id.selectCityText);
        changeCityText.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeonce));
        headerPage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeonce));
        gpsCoords = findViewById(R.id.gpsCoords);
        gpsCoords.setVisibility(View.INVISIBLE);
        nameCity = findViewById(R.id.editText2);
        nameCity.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeonce));
        nameCity.setText(new UserPrefs(this).GetDefaultCity());
        nameCity.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCity(nameCity.getText().toString());
                UpdateWidget();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.backBtn: {
                onBackPressed();
                break;
            }

            case R.id.gpsButton: {

                int permissionStatusGps = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                if ((permissionStatusGps != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_PERMISSION);
                    return;
                }

                gps = new GPSController(this);

                if (gps.canGetLocation())
                {
                    latitude = gps.latitude;
                    longitude = gps.longitude;
                    gpsCoords.setVisibility(View.VISIBLE);
                    String latString = String.format("%.2f",latitude);
                    String longString = String.format("%.2f",longitude);
                    gpsCoords.setText(latString+" "+longString);
                    GetGeoCoderData(this);

                }
                else
                {
                    gps.showSettingsAlert();
                }
                break;
            }
        }
    }

    private void updateCity(String inputCity)
    {
        new UserPrefs(this).SetCity(inputCity);

    }

    private void UpdateWidget()
    {
        //Обновляем информацию в самом виджете
        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }
    //Переводим данные с координат в название ближайшего населенного пункта через GeoCoder Google
    private void GetGeoCoderData(Context context) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = gcd.getFromLocation(latitude,longitude,2);
        } catch (IOException e) {
            e.printStackTrace();
            MakeToastMessage("Something went wrong.");
            return;
        }

        if (addresses.size() > 0) {
            nameCity.setText(addresses.get(0).getLocality());
            //Обновляем город в Prefs, для его загрузки в другом активити
            updateCity(nameCity.getText().toString());
            UpdateWidget();
        }
        else
        {
            MakeToastMessage("Information is not allowed..");
        }
    }

    private void MakeToastMessage(String message)
    {
        Toast toast=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

    public void OnClickLocaleRu(View v)
    {
        Locale locale = new Locale("ru");
        SetLocale(locale,"ru");
    }

    public void OnClickLocaleEn(View v)
    {
        Locale locale = new Locale("en");
        SetLocale(locale,"en");
    }

    private void SetLocale(Locale locale,String lang)
    {
        new UserPrefs(this).SetLang(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            getBaseContext().getResources()
                    .updateConfiguration(configuration,
                            getBaseContext()
                                    .getResources()
                                    .getDisplayMetrics());
            setTitle(R.string.app_name);
            //BUG: Recreate обновляет содержимое только со второго нажатия.
            recreate();
            MakeToastMessage(getString(R.string.RestartApp));
        }
        else
        {
            MakeToastMessage("Your API system is not allowed change locale in app..");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gps!=null)
        {
            gps.stopUsingGPS();
        }
    }
}
