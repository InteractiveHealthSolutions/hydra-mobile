package com.ihsinformatics.dynamicformsgenerator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppConfiguration {
    public static String getServerAddress(Context c) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        String serverAddress = "http://"
                + sharedPrefs.getString("serverIp", "ihs.ihsinformatics.com") // ird.irdresearch.org
                + ":"
                + sharedPrefs.getString("serverPort", "6928") // 9909
                + "/"
                + "openmrs/module";//"tbrmobileweb";
        return serverAddress;
    }
}
