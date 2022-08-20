package com.jvmfrog.ffsettings.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocaleManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;

import android.os.LocaleList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.FragmentSettingsBinding;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private int NIGHT_MODE = 0;
    private int APP_LANGUAGE = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        nightMode();
        setAppLanguage();

        return binding.getRoot();
    }

    private void nightMode() {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.S) {
            binding.autoRadioButton.setVisibility(View.GONE);
        }
        NIGHT_MODE = SharedPreferencesUtils.getInteger(getActivity(), "nightMode");
        switch (NIGHT_MODE) {
            case 0:
                binding.themesRadioGroup.check(R.id.autoRadioButton);
                break;
            case 1:
                binding.themesRadioGroup.check(R.id.systemRadioButton);
                break;
            case 2:
                binding.themesRadioGroup.check(R.id.lightRadioButton);
                break;
            case 3:
                binding.themesRadioGroup.check(R.id.darkRadioButton);
                break;
        }

        binding.themesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.autoRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", 0);
                        Toast.makeText(getActivity(), "Enabled auto night mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.systemRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", 1);
                        Toast.makeText(getActivity(), "Enabled system night mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.lightRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", 2);
                        Toast.makeText(getActivity(), "Enabled light mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.darkRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", 3);
                        Toast.makeText(getActivity(), "Enabled night mode", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
    private void setAppLanguage() {
        APP_LANGUAGE = SharedPreferencesUtils.getInteger(getActivity(), "language");
        switch (APP_LANGUAGE) {
            case 0:
                binding.languagesRadioGroup.check(R.id.enLanguageRadioButton);
                break;
            case 1:
                binding.languagesRadioGroup.check(R.id.beLanguageRadioButton);
                break;
            case 2:
                binding.languagesRadioGroup.check(R.id.ruLanguageRadioButton);
                break;
            case 3:
                binding.languagesRadioGroup.check(R.id.ukLanguageRadioButton);
                break;
            case 4:
                binding.languagesRadioGroup.check(R.id.trLanguageRadioButton);
                break;
        }

        binding.languagesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.enLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", 0);
                        break;
                    case R.id.beLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", 1);
                        getActivity().getSystemService(LocaleManager.class)
                                .setApplicationLocales(new LocaleList(Locale.forLanguageTag("ru-rBY")));
                        break;
                    case R.id.ruLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", 2);
                        getActivity().getSystemService(LocaleManager.class)
                                .setApplicationLocales(new LocaleList(Locale.forLanguageTag("ru-rRU")));                        break;
                    case R.id.ukLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", 3);
                        getActivity().getSystemService(LocaleManager.class)
                                .setApplicationLocales(new LocaleList(Locale.forLanguageTag("ru-rUA")));                        break;
                    case R.id.trLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", 4);
                        getActivity().getSystemService(LocaleManager.class)
                                .setApplicationLocales(new LocaleList(Locale.forLanguageTag("tr-rTR")));                        break;
                }
            }
        });
    }
}