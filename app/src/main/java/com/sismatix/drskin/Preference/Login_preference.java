package com.sismatix.drskin.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Login_preference {
    public static SharedPreferences mPrefs;
    public static SharedPreferences.Editor prefsEditor;

    public static void setLogin_flag(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("login_flag", value);
        prefsEditor.commit();
    }
    public static String getLogin_flag(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("login_flag", "");
    }


    public static void setCart_item_count(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("item_count", value);
        prefsEditor.commit();
    }
    public static String getCart_item_count(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("item_count", "");
    }


    public static void setcustomer_id(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("customer_id", value);
        prefsEditor.commit();
    }
    public static String getcustomer_id(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("customer_id", "");
    }
    public static void setemail(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("email", value);
        prefsEditor.commit();
    }
    public static String getemail(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("email", "");
    }
    public static void setfullname(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("fullname", value);
        prefsEditor.commit();
    }
    public static String getfullname(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("fullname", "");
    }
    public static void setiteamqty(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("items_qty", value);
        prefsEditor.commit();
    }
    public static String getiteamqty(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("items_qty", "");
    }

    public static void setquote_id(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("quote_id", value);
        prefsEditor.commit();
    }
    public static String getquote_id(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("quote_id", "");
    }

    public static void setdevicetoken(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("devicetoken", value);
        prefsEditor.commit();
    }
    public static String getdevicetoken(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("devicetoken", "");
    }

}
