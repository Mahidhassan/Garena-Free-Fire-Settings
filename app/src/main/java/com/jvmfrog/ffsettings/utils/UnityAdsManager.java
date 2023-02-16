package com.jvmfrog.ffsettings.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
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
    private final Activity activity;
    private final String APP_ID = "5075999";
    private final String BANNER_AD_ID = "Banner_Android";
    private final String REWARD_AD_ID = "Rewarded_Android";
    private final String INTERSTITIAL_AD_ID = "Interstitial_Android";
    private final Boolean TEST_MODE = true;

    public UnityAdsManager(Activity activity) {
        instance = this;
        this.activity = activity;
        UnityAds.initialize(activity.getApplicationContext(), APP_ID, TEST_MODE, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.d("UnityAds", "Successfully initialization complete");
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String message) {
                Log.d("UnityAds", unityAdsInitializationError.toString() + ":\n" + message);
            }
        });

        UnityAds.load(INTERSTITIAL_AD_ID, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAds", "The banner:" + placementId + " " + "successfully loaded.");
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError unityAdsLoadError, String message) {
                Log.e("UnityAds", "failed to load ad for " + placementId + " with error: [" + unityAdsLoadError + "] " + message);
            }
        });
    }

    public void showBannerAd(FrameLayout bannerContainer) {
        BannerView bannerView = new BannerView(activity, BANNER_AD_ID, new UnityBannerSize(320, 50));
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
                showBannerAd(bannerContainer);
            }
        });
    }

    public void showInterstitialAd() {
        UnityAds.show(activity, INTERSTITIAL_AD_ID, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError unityAdsShowError, String message) {
                Log.e("UnityAds", "failed to show ad for " + INTERSTITIAL_AD_ID + " with error: [" + unityAdsShowError + "] " + message);
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
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                Log.v("UnityAds", "onUnityAdsShowComplete: " + placementId);
            }
        });
    }
}
