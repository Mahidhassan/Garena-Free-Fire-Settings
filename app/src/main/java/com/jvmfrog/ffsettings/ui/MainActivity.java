package com.jvmfrog.ffsettings.ui;

import static com.jvmfrog.ffsettings.R.*;

import android.app.Application;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.jvmfrog.ffsettings.BuildConfig;
import com.jvmfrog.ffsettings.MyApplication;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.ActivityMainBinding;
import com.jvmfrog.ffsettings.ui.dialog.AboutAdsDialog;
import com.jvmfrog.ffsettings.utils.BannerAdHelper;
import com.jvmfrog.ffsettings.utils.InterstitialAdHelper;
import com.jvmfrog.ffsettings.utils.NavigationUtils;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;
import com.jvmfrog.ffsettings.utils.UMPHelper;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public InterstitialAdHelper interstitialAdHelper;
    private Boolean isFirstOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstOpen = SharedPreferencesUtils.getBoolean(this, "isFirstOpen");
        Application application = getApplication();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomAppBar();

        if (BuildConfig.BUILD_TYPE != "pro") {
            new UMPHelper(this).initConsent();
            ((MyApplication) application).showAdIfAvailable(this, () -> {new AboutAdsDialog(this).materialAlertDialogBuilder();});
            new BannerAdHelper(this).init(binding.bannerAd);
            interstitialAdHelper = new InterstitialAdHelper(this);
            interstitialAdHelper.loadInterstitialAd();
        }

        if (!isFirstOpen) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, style.Theme_FFSettings_MaterialAlertDialog));
            builder.setIcon(drawable.ic_round_insert_emoticon_24);
            builder.setTitle(string.welcome);
            builder.setMessage(string.welcome_message);
            builder.setPositiveButton("OK", (dialog, which) -> {
                isFirstOpen = true;
                SharedPreferencesUtils.saveBoolean(this, "isFirstOpen", true);
            });
            builder.show();
        }
    }

    private void bottomAppBar() {
        binding.bottomAppBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case id.home:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            id.nav_host_fragment,
                            id.manufacturerFragment,
                            null,
                            anim.enter_from_left, anim.exit_to_right,
                            anim.enter_from_right, anim.exit_to_left);
                    break;
                case id.about_app:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            id.nav_host_fragment,
                            id.aboutAppFragment,
                            null,
                            anim.enter_from_right, anim.exit_to_left,
                            anim.enter_from_left, anim.exit_to_right);
                    break;
            }
            return true;
        });

        binding.bottomAppBar.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case id.home:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            id.nav_host_fragment,
                            id.manufacturerFragment);
                    break;
                case id.about_app:
                    NavigationUtils.navigateWithNavHost(
                            this,
                            id.nav_host_fragment,
                            id.aboutAppFragment);
                    break;
            }
        });
    }
}