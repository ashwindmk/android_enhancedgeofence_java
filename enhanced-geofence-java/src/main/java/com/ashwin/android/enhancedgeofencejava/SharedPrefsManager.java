package com.ashwin.android.enhancedgeofencejava;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

class SharedPrefsManager {
    private static final String ENHANCED_GEOFENCE_PREFERENCES = "enhanced_geofence_preferences";

    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences mSharedPrefs;
    private static SharedPrefsManager mSharedPrefsManager;

    /**
     * Keys
     */
    static final String LAST_ENTERED_GEOFENCES = "last_entered_geofences";
    static final String REGISTERED_GEOFENCES = "registered_geofences";

    private SharedPrefsManager(Context context) {
        mSharedPrefs = context.getSharedPreferences(ENHANCED_GEOFENCE_PREFERENCES, Context.MODE_PRIVATE);
    }

    private static void initialize(Context context) {
        synchronized (SharedPrefsManager.class) {
            if (mSharedPrefsManager == null) {
                mSharedPrefsManager = new SharedPrefsManager(context);
            }
        }
    }

    static SharedPrefsManager get(Context context) {
        if (mSharedPrefsManager == null) {
            initialize(context);
        }
        return mSharedPrefsManager;
    }

    private void doEdit() {
        if (mEditor == null) {
            mEditor = mSharedPrefs.edit();
        }
    }

    private void doCommit() {
        if (mEditor != null) {
            mEditor.apply();
            mEditor = null;
        }
    }

    void put(String key, Set<String> value) {
        doEdit();
        mEditor.putStringSet(key, value);
        doCommit();
    }

    Set<String> getStringSet(String key, Set<String> defaultValue) {
        return new HashSet<String>(mSharedPrefs.getStringSet(key, defaultValue));
    }
}
