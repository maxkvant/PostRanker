package com.example.maxim.shortstories2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefs {
    public static String getString(Context ctx, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(key, null);
    }

    public static void storeString(Context ctx, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.apply();
    }
}
