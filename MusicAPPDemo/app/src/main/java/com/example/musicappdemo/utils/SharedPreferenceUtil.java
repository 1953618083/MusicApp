package com.example.musicappdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void init(Context context, String name) {
        preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static void clearSharedPreferences(){
        editor.clear();
    }

}