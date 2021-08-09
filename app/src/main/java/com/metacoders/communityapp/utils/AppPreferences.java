package com.metacoders.communityapp.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private final static String PREFS_FILE = "app_prefs";
    private final static String KEY_CACHE_LOCATION = "key_cache_location";
    private SharedPreferences prefs;

    public AppPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static String getAccessToken(Context context) {
        return SharedPrefManager.getInstance(context).getUserToken();
    }

    public int getCacheLocation() {
        return prefs.getInt(KEY_CACHE_LOCATION, 0);
    }

    public void setCacheLocation(int cacheLocation) {
        prefs.edit().putInt(KEY_CACHE_LOCATION, cacheLocation).apply();
    }
}