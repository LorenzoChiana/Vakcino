package com.example.loren.vaccinebooklet.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class Utils {

    private static final String SP = "vakcino_sp";
    private static final String SP_LOGGED = "sp_logged";
    private static final String SP_ACCOUNT = "email";

    private Utils() {
    }


    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(SP, Context.MODE_PRIVATE);
    }

    public static void setLogged(Context context, boolean logged) {
        getPref(context).edit().putBoolean(SP_LOGGED, logged).apply();
    }

    public static boolean getLogged(Context context) {
        return getPref(context).getBoolean(SP_LOGGED, false);
    }

    public static void setAccount(Context context, String email) {
        getPref(context).edit().putString(SP_ACCOUNT, email).apply();
    }

    public static String getAccount(Context context) {
        return getPref(context).getString(SP_ACCOUNT, "");
    }
}

