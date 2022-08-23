package com.jvmfrog.ffsettings.utils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

public class LocaleHelper {
    public @interface Locale {
        LocaleListCompat ENGLISH = LocaleListCompat.forLanguageTags("en");
        LocaleListCompat BELARUSIAN = LocaleListCompat.forLanguageTags("en");;
        LocaleListCompat RUSSIAN = LocaleListCompat.forLanguageTags("en");;
        LocaleListCompat UKRAINIAN = LocaleListCompat.forLanguageTags("en");;
        LocaleListCompat TURKISH = LocaleListCompat.forLanguageTags("en");;
    }
}
