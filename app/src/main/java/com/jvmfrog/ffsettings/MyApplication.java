package com.jvmfrog.ffsettings;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.DynamicColorsOptions;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

public class MyApplication extends Application {
    public static MyApplication instance = null;
    public static Context context;

    public static MyApplication getInstance() {
        if (instance == null)
            instance = new MyApplication();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        int nightMode = SharedPreferencesUtils.getInteger(this, "nightMode");

        switch (nightMode) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

        if (!SharedPreferencesUtils.getBoolean(this, "useDynamicColors"))
            DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
