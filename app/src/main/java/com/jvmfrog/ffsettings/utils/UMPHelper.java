package com.jvmfrog.ffsettings.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.jvmfrog.ffsettings.R;

public class UMPHelper {
    private Context context;
    private ConsentInformation consentInformation;

    public UMPHelper(Context context) {
        this.context = context;
    }

    public void initConsent() {
        // Set tag for underage of consent. false means users are not underage.
        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                .setAdMobAppId(context.getString(R.string.admob_app_id))
                .setTagForUnderAgeOfConsent(false)
                .build();

        // Debug settings for Form
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(context)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("AF0F2B6E3BCDC6ACBFD315C64B00")
                .build();

        ConsentRequestParameters debugParams = new ConsentRequestParameters.Builder()
                .setConsentDebugSettings(debugSettings)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(context);
        consentInformation.requestConsentInfoUpdate((Activity) context, params, () -> {
                    // The consent information state was updated.
                    // You are now ready to check if a form is available.
                    if (consentInformation.isConsentFormAvailable()) {
                        UserMessagingPlatform.loadConsentForm(context, consentForm -> {
                                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.UNKNOWN) {
                                        consentForm.show((Activity) context, formError -> initConsent());
                                    } if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                                        consentForm.show((Activity) context, formError -> initConsent());
                                    }
                                }, formError -> {
                                    // Handle the error
                                }
                        );
                    }
                }, formError -> {/*Handle the error*/}
        );
    }
}
