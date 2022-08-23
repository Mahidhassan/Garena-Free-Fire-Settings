package com.jvmfrog.ffsettings.ui;

import android.app.Application;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.jvmfrog.ffsettings.MyApplication;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.ActivityMainBinding;
import com.jvmfrog.ffsettings.ui.fragment.AboutAppFragment;
import com.jvmfrog.ffsettings.ui.fragment.ManufacturerFragment;
import com.jvmfrog.ffsettings.utils.FragmentUtils;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ConsentInformation consentInformation;

    private Boolean isFirstOpen;
    private int showReviewCount = 0;

    private static final int UPDATE_CODE = 100;
    private AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstOpen = SharedPreferencesUtils.getBoolean(this, "isFirstOpen");
        showReviewCount = SharedPreferencesUtils.getInteger(this, "showReviewCount");
        Application application = getApplication();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentUtils.changeFragment(this, new ManufacturerFragment(), R.id.frame, null);
        bottomAppBar();
        firstOpenDialog();
        initConsent();

        if (isFirstOpen) {
            ((MyApplication) application).showAdIfAvailable(this, () -> {
                //
            });
        }

        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
            }
        });

        try {
            appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    UPDATE_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bottomAppBar() {
        binding.bottomAppBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    FragmentUtils.changeFragmentWithAnimTwo(this, new ManufacturerFragment(), R.id.frame);
                    break;
                case R.id.about_app:
                    FragmentUtils.changeFragmentWithAnimOne(this, new AboutAppFragment(), R.id.frame);
                    break;
            }
            return true;
        });

        binding.bottomAppBar.setOnItemReselectedListener(item -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.home:
                    transaction.replace(R.id.frame, new ManufacturerFragment());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                    break;
                case R.id.about_app:
                    transaction.replace(R.id.frame, new AboutAppFragment());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                    break;
            }
        });
    }

    private void firstOpenDialog() {
        if (!isFirstOpen) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.Theme_FFSettings_MaterialAlertDialog));
            builder.setIcon(R.drawable.ic_round_insert_emoticon_24);
            builder.setTitle(R.string.welcome);
            builder.setMessage(R.string.welcome_message);
            builder.setPositiveButton("OK", (dialog, which) -> {
                isFirstOpen = true;
                SharedPreferencesUtils.saveBoolean(this, "isFirstOpen", true);
            });
            builder.show();
        }
    }

    private void initConsent() {
        // Set tag for underage of consent. false means users are not underage.
        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                .setAdMobAppId(getString(R.string.admob_app_id))
                .setTagForUnderAgeOfConsent(false)
                .build();

        // Debug settings for Form
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("AF0F2B6E3BCDC6ACBFD315C64B00")
                .build();

        ConsentRequestParameters debugParams = new ConsentRequestParameters.Builder()
                .setConsentDebugSettings(debugSettings)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(this, params, () -> {
                    // The consent information state was updated.
                    // You are now ready to check if a form is available.
                    if (consentInformation.isConsentFormAvailable()) {
                        UserMessagingPlatform.loadConsentForm(this, consentForm -> {
                                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.UNKNOWN) {
                                        consentForm.show(this, formError -> initConsent());
                                    }
                                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                                        consentForm.show(this, formError -> initConsent());
                                    }
                                }, formError -> {
                                    // Handle the error
                                }
                        );
                    }
                }, formError -> {/*Handle the error*/}
        );
    }

    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.
    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(
                appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        // If an in-app update is already running, resume the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.IMMEDIATE,
                                    this,
                                    UPDATE_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}