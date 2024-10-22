package com.example.datn_toystoryshop.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.R;

import java.util.Locale;

public class Currency_Language_screen extends AppCompatActivity {
    private Spinner spinnerLanguage;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "LanguageSettings";
    private static final String KEY_LANGUAGE = "current_language";
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_language_screen);

    }

}