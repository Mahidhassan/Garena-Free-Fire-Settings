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

    public static MyApplication getInstance() {
        if (instance == null)
            instance = new MyApplication();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        int nightMode = SharedPreferencesUtils.getInteger(this, "nightMode");
        int[] mode = {AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.MODE_NIGHT_YES};
        AppCompatDelegate.setDefaultNightMode(mode[nightMode]);
    }
}
