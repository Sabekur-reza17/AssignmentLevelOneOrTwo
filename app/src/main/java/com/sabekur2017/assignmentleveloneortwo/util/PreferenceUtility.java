package com.sabekur2017.assignmentleveloneortwo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtility {
    private static final String LOGGED_IN_TOKEN = "logged_in_token";
    private static final String DEFAULT_TOKEN = "";
    private static final Object LOCK = new Object();
    private static SharedPreferences sInstance;

    private static SharedPreferences getPreferences(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = PreferenceManager.getDefaultSharedPreferences(context);
            }
        }
        return sInstance;
    }
    public static void setLoggedInToken(Context context,String token) {
        SharedPreferences.Editor editor = getPreferences(context).edit();

        editor.putString(PreferenceUtility.LOGGED_IN_TOKEN, token);
        editor.apply();
    }
    public static String getLoggedInToken(Context context){
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(LOGGED_IN_TOKEN, DEFAULT_TOKEN);
    }

}
