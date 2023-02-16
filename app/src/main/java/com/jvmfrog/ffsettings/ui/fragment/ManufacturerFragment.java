package com.jvmfrog.ffsettings.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManufacturerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String name = SharedPreferencesUtils.getString(requireActivity(), "user_name");
        StringBuilder userName = new StringBuilder(getString(R.string.welcome) + "," + "\n" + name + "!");
        StringBuilder defaultUserName = new StringBuilder(getString(R.string.welcome) + "," + "\n" + getString(R.string.user_name) + "!");

        binding.welcomeAndUserName.setText(name.equals("") ? defaultUserName : userName);

        if (NetworkCheckHelper.isNetworkAvailable(requireActivity())) {
            new ManufacturerHelper().getManufacturersFromURL(
                    getActivity(),
                    ManufacturerFragment.this,
                    binding.recview,
                    binding.shimmerLayout
            );
        } else {
            new ManufacturerHelper().getManufacturersFromAssets(
                    getActivity(),
                    ManufacturerFragment.this,
                    binding.recview,
                    binding.shimmerLayout
            );
        }

        binding.setUserNameBtn.setOnClickListener(view1 -> ChangeUsernameDialog.showDialog(getActivity()));
        binding.googleFormBtn.setOnClickListener(view1 -> new CustomTabUtil().OpenCustomTab(getActivity(), "https://t.me/freefiresettingsapp", R.color.md_theme_light_onSecondary));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}