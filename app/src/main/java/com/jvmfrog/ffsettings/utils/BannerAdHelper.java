package com.jvmfrog.ffsettings.utils;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.jvmfrog.ffsettings.BuildConfig;

public class BannerAdHelper {
    private Context context;
    private AdRequest adRequest;

    public BannerAdHelper(Context context) {
        this.context = context;
    }

    public void init(AdView adView) {
        if (BuildConfig.BUILD_TYPE != "pro") {
            MobileAds.initialize(context, initializationStatus -> {});
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    adView.setVisibility(View.GONE);
                }
            });
        }
    }
}
