package com.jvmfrog.ffsettings.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jvmfrog.ffsettings.databinding.FragmentDevicesBinding;
import com.jvmfrog.ffsettings.utils.NetworkCheckHelper;
import com.jvmfrog.ffsettings.utils.SensitivitiesHelper;

public class DevicesFragment extends Fragment {
    private FragmentDevicesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle finalBundle = new Bundle();
        finalBundle.putAll(getArguments());

        if (NetworkCheckHelper.isNetworkAvailable(getActivity())) {
            new SensitivitiesHelper().getSensitivitiesFromURL(
                    getActivity(),
                    DevicesFragment.this,
                    binding.recview,
                    finalBundle.getString("model"),
                    binding.shimmerLayout
            );
        } else {
            new SensitivitiesHelper().getSensitivitiesFromAssets(
                    getActivity(),
                    DevicesFragment.this,
                    binding.recview,
                    finalBundle.getString("model"),
                    binding.shimmerLayout
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}