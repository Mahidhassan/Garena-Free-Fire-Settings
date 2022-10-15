package com.jvmfrog.ffsettings.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.ParamsModel;
import com.jvmfrog.ffsettings.ui.fragment.DeviceSettingsFragment;
import com.jvmfrog.ffsettings.utils.FragmentUtils;

import java.util.List;
import java.util.Locale;

public class DevicesAdapter extends FirestoreRecyclerAdapter<ParamsModel, DevicesAdapter.holder> {

    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";
    private static final String TAG = "AdMobFragment";
    private InterstitialAd mInterstitialAd;
    private Context context;

    public DevicesAdapter(@NonNull FirestoreRecyclerOptions<ParamsModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DevicesAdapter.holder holder, int position, @NonNull ParamsModel model) {
        holder.device_name.setText(model.getDevice_name());

        loadInterstitialAd(context);

        holder.itemView.setOnClickListener(v -> {
            showInterstitial(context);
            Bundle finalBundle = new Bundle();
            finalBundle.putFloat("review", model.getReview());
            finalBundle.putFloat("collimator", model.getCollimator());
            finalBundle.putFloat("x2_scope", model.getX2_scope());
            finalBundle.putFloat("x4_scope", model.getX4_scope());
            finalBundle.putFloat("sniper_scope", model.getSniper_scope());
            finalBundle.putFloat("free_review", model.getFree_review());
            finalBundle.putFloat("dpi", model.getDpi());
            finalBundle.putFloat("fire_button", model.getFire_button());
            finalBundle.putString("settings_source_url", model.getSettings_source_url());
            FragmentUtils.changeFragmentWithBackStack((FragmentActivity) v.getContext(), new DeviceSettingsFragment(), R.id.frame, "back", finalBundle);
        });
    }

    @NonNull
    @Override
    public DevicesAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new holder(view);
    }

    static class holder extends RecyclerView.ViewHolder {
        TextView device_name;
        public holder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.categories);
        }
    }

    private void loadInterstitialAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.admob_interstellar_test_ad_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        Toast.makeText(context, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;

                        String error = String.format(
                                Locale.ENGLISH,
                                "domain: %s, code: %d, message: %s",
                                loadAdError.getDomain(),
                                loadAdError.getCode(),
                                loadAdError.getMessage());
                        Toast.makeText(
                                        context,
                                        "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void showInterstitial(Context context) {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && context != null) {
            mInterstitialAd.show((Activity) context);
        } else {
            Toast.makeText(context, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }
}
