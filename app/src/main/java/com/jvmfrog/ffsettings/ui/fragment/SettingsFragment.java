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
        binding.themesRadioGroup.check(NIGHT_MODE);

        binding.themesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.autoRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", id);
                        Toast.makeText(getActivity(), "Enabled auto night mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.systemRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", id);
                        Toast.makeText(getActivity(), "Enabled system night mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.lightRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", id);
                        Toast.makeText(getActivity(), "Enabled light mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.darkRadioButton:
                        getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        SharedPreferencesUtils.saveInteger(getActivity(), "nightMode", id);
                        Toast.makeText(getActivity(), "Enabled night mode", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
    private void setAppLanguage() {
        APP_LANGUAGE = SharedPreferencesUtils.getInteger(getActivity(), "language");
        binding.languagesRadioGroup.check(APP_LANGUAGE);
        LocaleListCompat enLocale = LocaleListCompat.forLanguageTags("en");
        LocaleListCompat beLocale = LocaleListCompat.forLanguageTags("be");
        LocaleListCompat ruLocale = LocaleListCompat.forLanguageTags("ru");
        LocaleListCompat uaLocale = LocaleListCompat.forLanguageTags("uk");
        LocaleListCompat trLocale = LocaleListCompat.forLanguageTags("tr");

        binding.languagesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                // Call this on the main thread as it may require Activity.restart()
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.enLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", id);
                        AppCompatDelegate.setApplicationLocales(enLocale);
                        break;
                    case R.id.beLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", id);
                        AppCompatDelegate.setApplicationLocales(beLocale);
                        break;
                    case R.id.ruLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", id);
                        AppCompatDelegate.setApplicationLocales(ruLocale);
                        break;
                    case R.id.ukLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", id);
                        AppCompatDelegate.setApplicationLocales(uaLocale);
                        break;
                    case R.id.trLanguageRadioButton:
                        SharedPreferencesUtils.saveInteger(getActivity(), "language", id);
                        AppCompatDelegate.setApplicationLocales(trLocale);
                        break;
                }
            }
        });
    }
}