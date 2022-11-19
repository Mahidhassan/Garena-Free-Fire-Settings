package com.jvmfrog.ffsettings.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

public class ScreenshotEditionDialog {

    public static void showDialog(Activity activity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.Theme_FFSettings_MaterialAlertDialog));
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.screenshot_edition_layout, null);

        builder.setTitle("Screenshot Edition Config");
        builder.setMessage(
                "P.S. If for some reason you got here, then close this window and forget about it.\n" +
                "This is only for the developer.");
        builder.setIcon(R.drawable.ic_round_settings_24);
        builder.setView(dialogView);

        MaterialSwitch fakeAppNameSwitch = (MaterialSwitch) dialogView.findViewById(R.id.fakeAppNameSwitch);
        MaterialSwitch fakeManufacturerNamesSwitch = (MaterialSwitch) dialogView.findViewById(R.id.fakeManufacturerNamesSwitch);
        MaterialSwitch fakeMobileDeviceNamesSwitch = (MaterialSwitch) dialogView.findViewById(R.id.fakeMobileDeviceNamesSwitch);
        MaterialSwitch hideAdBannerSwitch = (MaterialSwitch) dialogView.findViewById(R.id.hideAdBannerSwitch);
        AdView adBanner = (AdView) activity.findViewById(R.id.bannerAd);

        fakeAppNameSwitch.setChecked(SharedPreferencesUtils.getBoolean(activity, "isFakeAppName"));
        fakeManufacturerNamesSwitch.setChecked(SharedPreferencesUtils.getBoolean(activity, "isFakeManufacturerNames"));
        fakeMobileDeviceNamesSwitch.setChecked(SharedPreferencesUtils.getBoolean(activity, "isFakeMobileDeviceNames"));
        hideAdBannerSwitch.setChecked(SharedPreferencesUtils.getBoolean(activity, "isAdBannerHidden"));

        fakeAppNameSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesUtils.saveBoolean(activity, "isFakeAppName", isChecked);
            MaterialToolbar app_name = (MaterialToolbar) activity.findViewById(R.id.toolbar);
            if (isChecked) {
                app_name.setTitle(R.string.fake_app_name);
            } else {
                app_name.setTitle(R.string.app_name);
            }
        });

        fakeManufacturerNamesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesUtils.saveBoolean(activity, "isFakeManufacturerNames", isChecked);
        });

        fakeMobileDeviceNamesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesUtils.saveBoolean(activity, "isFakeMobileDeviceNames", isChecked);
        });

        hideAdBannerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesUtils.saveBoolean(activity, "isAdBannerHidden", isChecked);
            if (isChecked) {
                adBanner.setVisibility(View.GONE);
            } else {
                adBanner.setVisibility(View.VISIBLE);
            }
        });

        builder.setPositiveButton("Close", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
