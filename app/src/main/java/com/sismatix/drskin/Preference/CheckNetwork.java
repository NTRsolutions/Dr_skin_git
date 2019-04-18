package com.sismatix.drskin.Preference;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {

    public static boolean backPressed = false;

    public static String backString = "";

    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();

            // ConnectivityManager cm;
            // cm = (ConnectivityManager)
            // con.getSystemService(Context.CONNECTIVITY_SERVICE);
            // if (cm.getActiveNetworkInfo() != null
            // && cm.getActiveNetworkInfo().isAvailable()
            // && cm.getActiveNetworkInfo().isConnected()) {
            //
            // return true;
            // }else{
            // return false;
            // }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
