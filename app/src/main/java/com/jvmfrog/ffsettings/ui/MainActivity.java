package com.jvmfrog.ffsettings.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.ActivityMainBinding;
import com.jvmfrog.ffsettings.utils.PerAppLanguageManager;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;
import com.jvmfrog.ffsettings.utils.UnityAdsManager;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    private UnityAdsManager unityAdsManager;
    private NavHostFragment navHostFragment;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        unityAdsManager = new UnityAdsManager(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();

        if (!SharedPreferencesUtils.getBoolean(this, "isFirstOpen")) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.Theme_FFSettings_MaterialAlertDialog));
            builder.setIcon(R.drawable.ic_round_insert_emoticon_24);
            builder.setTitle(R.string.welcome);
            builder.setMessage(R.string.welcome_message);
            builder.setPositiveButton("OK", (dialog, which) -> SharedPreferencesUtils.saveBoolean(this, "isFirstOpen", true));
            builder.show();
        }

        unityAdsManager.showBannerAd(binding.bannerAd);
        try {
            navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            assert navHostFragment != null;
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottomAppBar);
            NavigationUI.setupWithNavController(bottomNav, navController);
        } catch (Exception e) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("НА, ХАВАЙ")
                    .setMessage("Ошибка в этом:" + "\n" + e.getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int id = navHostFragment.getNavController().getCurrentDestination().getId();
        if (!(id == R.id.devicesFragment || id == R.id.deviceSettingsFragment)) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void recreate() {
        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
        super.recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}