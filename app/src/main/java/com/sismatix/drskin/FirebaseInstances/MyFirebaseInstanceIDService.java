package com.sismatix.drskin.FirebaseInstances;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sismatix.drskin.Preference.Login_preference;

/**
 * Created by ap6 on 17/12/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    String refreshedToken;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseApp.initializeApp(this);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("tokennn_test", "" + refreshedToken);
        Login_preference.setdevicetoken(this,refreshedToken);

// Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

// sending reg id to your server
        sendRegistrationToServer(refreshedToken);

// Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
// sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        Log.e("tokennn_sharedpref", "" + token);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}