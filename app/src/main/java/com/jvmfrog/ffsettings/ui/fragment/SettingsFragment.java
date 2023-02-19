package com.jvmfrog.ffsettings.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.color.DynamicColors;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.FragmentSettingsBinding;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container,false);
        binding.dynamicColorsSwitch.setEnabled(DynamicColors.isDynamicColorAvailable());
        binding.dynamicColorsSwitch.setChecked(SharedPreferencesUtils.getBoolean(requireContext(), "useDynamicColors"));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.appThemeRadioGroup.check(SharedPreferencesUtils.getInteger(requireContext(), "checkedButton", R.id.followSystemNightModeRadioButton));
        binding.appThemeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.followSystemNightModeRadioButton:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    SharedPreferencesUtils.saveInteger(requireContext(), "checkedButton", R.id.followSystemNightModeRadioButton);
                    SharedPreferencesUtils.saveInteger(requireContext(), "nightMode", 0);
                    requireActivity().recreate();
                    break;
                case R.id.noNightModeRadioButton:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPreferencesUtils.saveInteger(requireContext(), "checkedButton", R.id.noNightModeRadioButton);
                    SharedPreferencesUtils.saveInteger(requireContext(), "nightMode", 1);
                    requireActivity().recreate();
                    break;
                case R.id.nightModeRadioButton:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPreferencesUtils.saveInteger(requireContext(), "checkedButton", R.id.nightModeRadioButton);
                    SharedPreferencesUtils.saveInteger(requireContext(), "nightMode", 2);
                    requireActivity().recreate();
                    break;
            }
        });

        binding.dynamicColorsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesUtils.saveBoolean(requireContext(), "useDynamicColors", isChecked);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}