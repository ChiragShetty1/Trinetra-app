package com.capstone.objdetection;

import android.content.Context;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;


public class SessionClass {

    public static void saveMobileNumber(Context context, String number) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mob_no", number);
        editor.apply();
    }

    public static String getMobileNumber(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("mob_no", null);
    }
}