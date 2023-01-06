package com.jvmfrog.ffsettings.ui;

import android.app.Application;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.AppBarConfigurationKt;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application application = getApplication();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!BuildConfig.BUILD_TYPE.equals("pro")) {
            new UMPHelper(this).initConsent();
            ((MyApplication) application).showAdIfAvailable(this, () -> {});
            new BannerAdHelper(this).init(binding.bannerAd);
        }

        if (!SharedPreferencesUtils.getBoolean(this, "isFirstOpen")) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.Theme_FFSettings_MaterialAlertDialog));
            builder.setIcon(R.drawable.ic_round_insert_emoticon_24);
            builder.setTitle(R.string.welcome);
            builder.setMessage(R.string.welcome_message);
            builder.setPositiveButton("OK", (dialog, which) -> SharedPreferencesUtils.saveBoolean(this, "isFirstOpen", true));
            builder.show();
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomAppBar);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }
}