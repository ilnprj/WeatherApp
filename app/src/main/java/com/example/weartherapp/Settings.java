package com.example.weartherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.util.Log;
public class Settings extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences userPrefs;
    TextView text;
    Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Задействуем первоначальную реализацию родителя
        super.onCreate(savedInstanceState);
        //Задаем интерфейс в Activity
        setContentView(R.layout.activity_settings);
        //Инициализация содержимого в интерфейсе
        setButtons();
        initPrefs();
        setTextViews();
    }

    private void setButtons()
    {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    }

    private void setTextViews()
    {
        text = findViewById(R.id.editText2);
        text.setText(getDefaultCity());
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setCity(text.getText().toString());
            }
        });
    }

    private void initPrefs()
    {
        userPrefs = getApplicationContext().getSharedPreferences("MyPrefs",MODE_PRIVATE);
    }


    @Override
    public void onClick(View v)
    {
        onBackPressed();
    }

    String getDefaultCity()
    {
        return this.userPrefs.getString("city", "Berlin, DE");
    }

    private void setCity(String city)
    {
        this.userPrefs.edit().putString("city",city).commit();
    }


}
