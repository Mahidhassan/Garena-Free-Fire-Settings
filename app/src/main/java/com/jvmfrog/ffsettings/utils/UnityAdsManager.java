package com.jvmfrog.ffsettings.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jvmfrog.ffsettings.ui.dialog.ErrorDialog;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

public class UnityAdsManager {
    public static UnityAdsManager instance;
    private final Activity activity2;
    private final String APP_ID = "5075999";
    private final String BANNER_AD_ID = "Banner_Android";
    private final String REWARD_AD_ID = "Rewarded_Android";
    private final String INTERSTITIAL_AD_ID = "Interstitial_Android";
    private final Boolean TEST_MODE = true;

    public UnityAdsManager(Activity activity) {
        instance = this;
        this.activity2 = activity;
        UnityAds.initialize(activity2.getApplicationContext(), APP_ID, TEST_MODE, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.d("UnityAds", "Successfully initialization complete");
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String message) {
                Log.d("UnityAds", unityAdsInitializationError.toString() + ":\n" + message);
                new ErrorDialog(activity2).showWith("Error of the UnityAds", unityAdsInitializationError.toString() + ":\n" + message);
            }
        });

        //loadAd(INTERSTITIAL_AD_ID);
    }

    public void loadAd(String AD_ID) {
        UnityAds.load(AD_ID, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAds", "The banner:" + placementId + " " + "successfully loaded.");
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError unityAdsLoadError, String message) {
                Log.e("UnityAds", "failed to load ad for " + placementId + " with error: [" + unityAdsLoadError + "] " + message);
                new ErrorDialog(activity2).showWith("Error of the UnityAds", "Failed to load ad for " + placementId + " with error: [" + unityAdsLoadError + "] " + message);
            }
        });
    }

    public void showBannerAd(FrameLayout bannerContainer) {
        BannerView bannerView = new BannerView(activity2, BANNER_AD_ID, new UnityBannerSize(320, 50));
        bannerContainer.addView(bannerView);
        bannerView.load();
        bannerView.setListener(new BannerView.Listener() {
            @Override
            public void onBannerLoaded(BannerView bannerAdView) {
                super.onBannerLoaded(bannerAdView);
                Log.d("UnityAds", "the banner:" + bannerAdView.getPlacementId() + " " + "successfully loaded.");
            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                super.onBannerFailedToLoad(bannerAdView, errorInfo);
                Log.d("UnityAds", "error of loading the banner: " + bannerAdView.getPlacementId() + " with error [" + errorInfo + "]");
                new ErrorDialog(activity2).showWith("Error of the UnityAds", "Error of loading the banner: " + bannerAdView.getPlacementId() + " with error [" + errorInfo + "]");
            }
        });
    }

    public void showInterstitialAd() {
        UnityAds.show(activity2, INTERSTITIAL_AD_ID, new UnityAdsShowOptions(), showListener());
    }

    private IUnityAdsShowListener showListener() {
        return new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                new ErrorDialog(activity2).showWith("Error of the UnityAds", "Failed to show ad for " + INTERSTITIAL_AD_ID + " with error: [" + error + "] " + message);
                if (error == UnityAds.UnityAdsShowError.NOT_INITIALIZED)
                    Toast.makeText(activity2, "Error related to SDK not initialized", Toast.LENGTH_SHORT).show();
                if (error == UnityAds.UnityAdsShowError.NOT_READY)
                    Toast.makeText(activity2, "Error related to placement not being ready", Toast.LENGTH_SHORT).show();
                if (error == UnityAds.UnityAdsShowError.VIDEO_PLAYER_ERROR)
                    Toast.makeText(activity2, "Error related to the video player", Toast.LENGTH_SHORT).show();
                if (error == UnityAds.UnityAdsShowError.INVALID_ARGUMENT)
                    Toast.makeText(activity2, "Error related to invalid arguments", Toast.LENGTH_SHORT).show();
                if (error == UnityAds.UnityAdsShowError.NO_CONNECTION)
                    Toast.makeText(activity2, "Error related to internet connection", Toast.LENGTH_SHORT).show();
                if (error == UnityAds.UnityAdsShowError.ALREADY_SHOWING)
                    Toast.makeText(activity2, "Error related to ad is already being showed", Toast.LENGTH_SHORT).show();
                if (error == UnityAds.UnityAdsShowError.INTERNAL_ERROR)
                    Toast.makeText(activity2, "Error related to environment or internal services", Toast.LENGTH_SHORT).show();
                if (error == UnityAds.UnityAdsShowError.TIMEOUT)
                    Toast.makeText(activity2, "Error related to an Ad being unable to show within a specified time frame", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
                Log.v("UnityAds", "onUnityAdsShowStart: " + placementId);
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                Log.v("UnityAds", "onUnityAdsShowClick: " + placementId);
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                if (state == UnityAds.UnityAdsShowCompletionState.COMPLETED) {
                    Log.d("UnityAds", "onUnityAdsShowComplete: " + placementId);
                }
                if (state == UnityAds.UnityAdsShowCompletionState.SKIPPED)
                    Log.d("d", "f");
            }
        };
    }
}
