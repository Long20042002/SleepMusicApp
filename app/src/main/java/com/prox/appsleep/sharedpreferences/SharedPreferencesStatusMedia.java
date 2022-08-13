package com.prox.appsleep.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesStatusMedia {

    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    public Context mContext;

    public SharedPreferencesStatusMedia(Context mContext) {
        this.mContext = mContext;
    }

    public void putBooleanValue(String key,boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void putIntValue(String key, int value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }
}
