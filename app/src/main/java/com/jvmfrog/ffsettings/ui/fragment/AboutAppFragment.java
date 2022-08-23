package com.jvmfrog.ffsettings.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jvmfrog.ffsettings.BuildConfig;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.FragmentAboutAppBinding;
import com.jvmfrog.ffsettings.utils.CustomTabUtil;
import com.jvmfrog.ffsettings.utils.BugReportHelper;
import com.jvmfrog.ffsettings.utils.OtherUtils;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

public class AboutAppFragment extends Fragment {

    private FragmentAboutAppBinding binding;
    int countToScreenshotEdition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutAppBinding.inflate(inflater, container, false);

        binding.appVersionBtn.setText(getString(R.string.version) + ": " + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
        binding.appVersionBtn.setOnClickListener(v -> {countToScreenshotEdition += 1;});
        binding.appVersionBtn.setOnLongClickListener(v -> {
            if (countToScreenshotEdition == 7 && !SharedPreferencesUtils.getBoolean(getActivity(), "isScreenshotEdition")) {
                SharedPreferencesUtils.saveBoolean(getActivity(), "isScreenshotEdition", true);
                Toast.makeText(getActivity(), "Enabled screenshot edition", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferencesUtils.saveBoolean(getActivity(), "isScreenshotEdition", false);
                Toast.makeText(getActivity(), "Disabled screenshot edition", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        binding.sourceCodeBtn.setOnClickListener(v -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.source_code_url), R.color.md_theme_light_onSecondary));
        binding.ibragimBtn.setOnClickListener(v -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.ibragim_url), R.color.md_theme_light_onSecondary));
        binding.mailBtn.setOnClickListener(v -> BugReportHelper.sendEmail(getActivity()));
        binding.rateBtn.setOnClickListener(v -> OtherUtils.reviewAppInGooglePlay(getActivity()));
        binding.vkGroupBtn.setOnClickListener(v -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.JVMFrog), R.color.md_theme_light_onSecondary));
        binding.otherAppsBtn.setOnClickListener(view1 -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:JVMFrog")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.GOOGLE_PLAY))));
            }
        });

        return binding.getRoot();
    }
}