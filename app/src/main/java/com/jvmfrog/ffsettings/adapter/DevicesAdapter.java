package com.jvmfrog.ffsettings.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.SensitivityModel;
import com.jvmfrog.ffsettings.utils.InterstitialAdHelper;
import com.jvmfrog.ffsettings.utils.NavigationUtils;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {

    private final Context context;
    private final Fragment fragment;
    private InterstitialAdHelper interstitialAdHelper;
    private final List<SensitivityModel> models;

    public DevicesAdapter(Context context, Fragment fragment, List<SensitivityModel> models) {
        this.context = context;
        this.fragment = fragment;
        this.models = models;
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesAdapter.DeviceViewHolder holder, int position) {
        holder.device_name.setText(models.get(position).getManufacturerName() + " " + models.get(position).getDeviceName());
        interstitialAdHelper = new InterstitialAdHelper(context);
        interstitialAdHelper.loadInterstitialAd();

        holder.itemView.setOnClickListener(v -> {
            interstitialAdHelper.showInterstitial();
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
            NavHostFragment.findNavController(fragment)
                    .navigate(R.id.action_devicesFragment_to_deviceSettingsFragment,
                            finalBundle);
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
