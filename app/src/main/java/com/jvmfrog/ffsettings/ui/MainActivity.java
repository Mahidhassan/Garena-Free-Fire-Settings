package com.jvmfrog.ffsettings.ui;

import android.app.Application;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jvmfrog.ffsettings.BuildConfig;
import com.jvmfrog.ffsettings.MyApplication;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.ActivityMainBinding;
import com.jvmfrog.ffsettings.utils.BannerAdHelper;
import com.jvmfrog.ffsettings.utils.InterstitialAdHelper;
import com.jvmfrog.ffsettings.utils.NavigationUtils;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;
import com.jvmfrog.ffsettings.utils.UMPHelper;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private InterstitialAdHelper interstitialAdHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application application = getApplication();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomAppBar();

        if (!BuildConfig.BUILD_TYPE.equals("pro")) {
            new UMPHelper(this).initConsent();
            ((MyApplication) application).showAdIfAvailable(this, () -> {});
            new BannerAdHelper(this).init(binding.bannerAd);
            interstitialAdHelper = new InterstitialAdHelper(this);
            interstitialAdHelper.loadInterstitialAd();
        }

        if (!SharedPreferencesUtils.getBoolean(this, "isFirstOpen")) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.Theme_FFSettings_MaterialAlertDialog));
            builder.setIcon(R.drawable.ic_round_insert_emoticon_24);
            builder.setTitle(R.string.welcome);
            builder.setMessage(R.string.welcome_message);
            builder.setPositiveButton("OK", (dialog, which) -> {
                SharedPreferencesUtils.saveBoolean(this, "isFirstOpen", true);
            });
            builder.show();
        }
    }

    private void bottomAppBar() {
        binding.bottomAppBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            R.id.nav_host_fragment,
                            R.id.manufacturerFragment,
                            null,
                            R.anim.enter_from_left, R.anim.exit_to_right,
                            R.anim.enter_from_right, R.anim.exit_to_left);
                    break;
                case R.id.about_app:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            R.id.nav_host_fragment,
                            R.id.aboutAppFragment,
                            null,
                            R.anim.enter_from_right, R.anim.exit_to_left,
                            R.anim.enter_from_left, R.anim.exit_to_right);
                    break;
            }
            return true;
        });

        binding.bottomAppBar.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            R.id.nav_host_fragment,
                            R.id.manufacturerFragment);
                    break;
                case R.id.about_app:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            R.id.nav_host_fragment,
                            R.id.aboutAppFragment);
                    break;
            }
        });
    }
}