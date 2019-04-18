package com.sismatix.drskin.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyAddress_Preference {
    public static SharedPreferences mPrefs;
    public static SharedPreferences.Editor prefsEditor;

    public static void setFirstname(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("first_name", value);
        prefsEditor.commit();
    }
    public static String getFirstname(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("first_name", "");
    }

    public static void setLastname(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("last_name", value);
        prefsEditor.commit();
    }
    public static String getLastname(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("last_name", "");
    }

    public static void setPhoneNumber(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("phone_number", value);
        prefsEditor.commit();
    }
    public static String getPhoneNumber(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("phone_number", "");
    }

    public static void setCompanyName(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("company_name", value);
        prefsEditor.commit();
    }
    public static String getCompanyName(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("company_name", "");
    }

    public static void setStreetAddress(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("street_address", value);
        prefsEditor.commit();
    }
    public static String getStreetAddress(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("street_address", "");
    }

    public static void setFax(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("fax", value);
        prefsEditor.commit();
    }
    public static String getFax(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("fax", "");
    }

    public static void setZipcode(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("zipcode", value);
        prefsEditor.commit();
    }
    public static String getZipcode(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("zipcode", "");
    }

    public static void setCity(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("city", value);
        prefsEditor.commit();
    }
    public static String getCity(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("city", "");
    }

    public static void setRegion(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("region", value);
        prefsEditor.commit();
    }
    public static String getRegion(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("region", "");
    }

    public static void setAddressId(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("AddressId", value);
        prefsEditor.commit();
    }
    public static String getAddressId(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("AddressId", "");
    }

    public static void setCountryId(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("CountryId", value);
        prefsEditor.commit();
    }
    public static String getCountryId(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("CountryId", "");
    }

    public static void setCountryPosition(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("CountryPosition", value);
        prefsEditor.commit();
    }
    public static String getCountryPosition(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("CountryPosition", "");
    }

    public static void setAppartment(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("apartment", value);
        prefsEditor.commit();
    }
    public static String getAppartment(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("apartment", "");
    }



}
