package com.jvmfrog.ffsettings.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.SensitivityModel;
import com.jvmfrog.ffsettings.utils.UnityAdsManager;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {
    private final Fragment fragment;
    private final List<SensitivityModel> models;

    public DevicesAdapter(Fragment fragment, List<SensitivityModel> models) {
        this.fragment = fragment;
        this.models = models;
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesAdapter.DeviceViewHolder holder, int position) {
        StringBuilder deviceName = new StringBuilder(models.get(position).getManufacturerName() + " " + models.get(position).getDeviceName());
        holder.device_name.setText(deviceName);

        holder.itemView.setOnClickListener(v -> {
            //UnityAdsManager.instance.showInterstitialAd();
            Bundle finalBundle = new Bundle();
            finalBundle.putFloat("review", models.get(position).getReview());
            finalBundle.putFloat("collimator", models.get(position).getCollimator());
            finalBundle.putFloat("x2_scope", models.get(position).getX2Scope());
            finalBundle.putFloat("x4_scope", models.get(position).getX4Scope());
            finalBundle.putFloat("sniper_scope", models.get(position).getSniperScope());
            finalBundle.putFloat("free_review", models.get(position).getFreeReview());
            finalBundle.putFloat("dpi", models.get(position).getDpi());
            finalBundle.putFloat("fire_button", models.get(position).getFireButton());
            finalBundle.putString("settings_source_url", models.get(position).getSettingsSourceUrl());
            NavController navController = Navigation.findNavController(fragment.requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_devicesFragment_to_deviceSettingsFragment, finalBundle);
        });
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView device_name;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.categories);
        }
    }
}
