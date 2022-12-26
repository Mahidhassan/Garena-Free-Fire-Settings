package com.jvmfrog.ffsettings.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.FragmentManufacturerBinding;
import com.jvmfrog.ffsettings.ui.dialog.ChangeUsernameDialog;
import com.jvmfrog.ffsettings.utils.CustomTabUtil;
import com.jvmfrog.ffsettings.utils.ManufacturerHelper;
import com.jvmfrog.ffsettings.utils.NetworkCheckHelper;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

public class ManufacturerFragment extends Fragment {
    private FragmentManufacturerBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManufacturerBinding.inflate(inflater, container, false);

        if (SharedPreferencesUtils.getString(getActivity(), "user_name") == null || SharedPreferencesUtils.getString(getActivity(), "user_name").equals("")) {
            binding.welcomeAndUserName.setText(getString(R.string.welcome) + "," + "\n" + getString(R.string.user_name) + "!");
        } else {
            binding.welcomeAndUserName.setText(getString(R.string.welcome) + "," + "\n" + SharedPreferencesUtils.getString(getActivity(), "user_name") + "!");
        }

        if (NetworkCheckHelper.isNetworkAvailable(getActivity())) {
            new ManufacturerHelper().getManufacturersFromURL(
                    getActivity(),
                    binding.recview,
                    binding.shimmerLayout
            );
        } else {
            new ManufacturerHelper().getManufacturersFromAssets(
                    getActivity(),
                    binding.recview,
                    binding.shimmerLayout
            );
        }

        binding.setUserNameBtn.setOnClickListener(view -> ChangeUsernameDialog.showDialog(getActivity()));
        binding.googleFormBtn.setOnClickListener(view -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.google_form), R.color.md_theme_light_onSecondary));

        return binding.getRoot();
    }
}