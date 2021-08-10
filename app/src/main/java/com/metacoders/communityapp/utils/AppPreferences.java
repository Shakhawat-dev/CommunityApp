package com.metacoders.communityapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.appcompat.app.ActionBar;

public class AppPreferences {

    private final static String PREFS_FILE = "app_prefs";
    private final static String KEY_CACHE_LOCATION = "key_cache_location";
    private SharedPreferences prefs;

    public AppPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static int getRandomMaterialColor(String typeColor, Context context) {
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    public static void setActionbarTextColor(ActionBar actBar, int color, String title) {

        Spannable spannablerTitle = new SpannableString(title);
        spannablerTitle.setSpan(new ForegroundColorSpan(color), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actBar.setTitle(spannablerTitle);

    }

    public static String localeToEmoji(String countryCode) {
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
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