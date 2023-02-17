package com.jvmfrog.ffsettings.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.SensitivityModel;
import com.jvmfrog.ffsettings.utils.UnityAdsManager;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Fragment fragment;
    private final List<SensitivityModel> models;
    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_BANNER = 1;

    public DevicesAdapter(Fragment fragment, List<SensitivityModel> models) {
        this.fragment = fragment;
        this.models = models;
    }

    @NonNull
    @Override
    public DeviceNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BANNER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_name_item, parent, false);
            return new DeviceNameViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_name_item, parent, false);
            return new DeviceNameViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SensitivityModel model = models.get(position);
        if (model == null)
            return;
        if (viewHolder.getItemViewType() == VIEW_TYPE_BANNER) {
            BannerViewHolder holder = (BannerViewHolder) viewHolder;
            BannerView banner = new BannerView(fragment.requireActivity(), "Banner_Android", new UnityBannerSize(320, 50));
            banner.load();
            holder.bannerAdContainer.addView(banner);
        } else if (viewHolder.getItemViewType() == VIEW_TYPE_DEFAULT) {
            DeviceNameViewHolder holder = (DeviceNameViewHolder) viewHolder;
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
    }

    @Override
    public int getItemCount() {
        int count = models.size(); // Общее количество элементов
        int bannerCount = count / 5; // Количество баннеров (один каждые 5 элементов)
        if (count % 5 == 0) {
            bannerCount--; // Если количество элементов меньше 5, последний баннер будет добавлен на позицию count
        }
        if (count >= 10) {
            int extraBannerCount = (count - 10) / 8; // Количество дополнительных баннеров (один каждые 8 элементов после первых 10 элементов)
            count += bannerCount + extraBannerCount; // Общее количество элементов с учетом баннеров
        } else {
            count++; // Если элементов меньше 10, добавляем баннер в конец
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || ((position + 1) % 5 == 0 && position < getItemCount() - 1) || (position == getItemCount() - 1 && getItemCount() < 10)) {
            return VIEW_TYPE_BANNER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    public static class DeviceNameViewHolder extends RecyclerView.ViewHolder {
        TextView device_name;
        public DeviceNameViewHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.categories);
        }
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        FrameLayout bannerAdContainer;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerAdContainer = itemView.findViewById(R.id.banner_ad_container);
        }
    }
}
