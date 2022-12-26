package com.jvmfrog.ffsettings.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.SensitivityModel;
import com.jvmfrog.ffsettings.utils.InterstitialAdHelper;
import com.jvmfrog.ffsettings.utils.NavigationUtils;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {

    private final Context context;
    private InterstitialAdHelper interstitialAdHelper;
    private int show_ad_by_x_click = 0;
    private final List<SensitivityModel> models;

    public DevicesAdapter(Context context, List<SensitivityModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesAdapter.DeviceViewHolder holder, int position) {
        holder.device_name.setText(models.get(position).getDeviceName());
        interstitialAdHelper = new InterstitialAdHelper(context);
        interstitialAdHelper.loadInterstitialAd();

        holder.itemView.setOnClickListener(v -> {
            interstitialAdHelper.showInterstitial();
            Bundle finalBundle = new Bundle();
            finalBundle.putFloat("review", models.get(position).getReview());
            finalBundle.putFloat("collimator", models.get(position).getCollimator());
            finalBundle.putFloat("x2_scope", models.get(position).getX2_scope());
            finalBundle.putFloat("x4_scope", models.get(position).getX4_scope());
            finalBundle.putFloat("sniper_scope", models.get(position).getSniper_scope());
            finalBundle.putFloat("free_review", models.get(position).getFree_review());
            finalBundle.putFloat("dpi", models.get(position).getDpi());
            finalBundle.putFloat("fire_button", models.get(position).getFire_button());
            finalBundle.putString("settings_source_url", models.get(position).getSettings_source_url());
            NavigationUtils.navigateWithNavHost(
                    (FragmentActivity) v.getContext(),
                    R.id.nav_host_fragment,
                    R.id.action_devicesFragment_to_deviceSettingsFragment,
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
