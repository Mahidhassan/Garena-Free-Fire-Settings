package com.jvmfrog.ffsettings.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.jvmfrog.ffsettings.BuildConfig;

import java.util.Locale;

public class InterstitialAdHelper {
    private static final String TAG = "Interstitial Ad";
    private final Context mContext;
    private InterstitialAd mInterstitialAd;

    public InterstitialAdHelper(Context context) {
        mContext = context;
    }

    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load (
                mContext,
                BuildConfig.BUILD_TYPE.equals("debug") ? "ca-app-pub-3940256099942544/1033173712" : "ca-app-pub-4193046598871025/7823313141",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.d(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback (
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;

                        String error = String.format(
                                Locale.ENGLISH,
                                "domain: %s, code: %d, message: %s",
                                loadAdError.getDomain(),
                                loadAdError.getCode(),
                                loadAdError.getMessage());

                        Log.d(TAG, "onAdFailedToLoad() with error: " + error);
                    }
                });
    }

    public void showInterstitial() {
        if (mInterstitialAd != null && mContext != null && !BuildConfig.BUILD_TYPE.equals("pro")) {
            mInterstitialAd.show((Activity) mContext);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
        }
    }
}
