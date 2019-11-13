package com.ashwin.android.enhancedgeofencejava;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import com.ashwin.android.diygeofencejava.DiyGeofence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class EnhancedGeofence {
    public static final String TAG = "enhanced-geofence";

    static {
        DiyGeofence.setGeofenceListener(new DiyGeofenceListenerImpl());
    }

    private static GeofenceManager mGeofenceManager = null;

    public static void init(Context context) {
        DiyGeofence.init(context);
        mGeofenceManager = GeofenceManager.get(context);
    }

    private static EnhancedGeofenceListener mEnhancedGeofenceListener = null;

    private static Handler mMainHandler = new Handler(Looper.getMainLooper());

    // Configurations
    public static void setLogLevel(int level) {
        DiyGeofence.setLogLevel(level);
    }

    public static void setAutoLocationUpdates(Context context, boolean enable) {
        DiyGeofence.setAutoLocationUpdates(context, enable);
    }

    public static void setGeofenceListener(EnhancedGeofenceListener listener) {
        mEnhancedGeofenceListener = listener;
    }

    // APIs
    public static boolean addGeofence(Context context, final String id, final double lat, final double lng, final float rad) {
        if (mGeofenceManager == null) {
            init(context);
        }

        Set<String> registeredGeofences = SharedPrefsManager.get(context).getStringSet(SharedPrefsManager.REGISTERED_GEOFENCES, new HashSet<String>());
        if (registeredGeofences.contains(id) && DiyGeofence.isGeofenceAdded(context, id, lat, lng, rad)) {
            return true;
        }

        boolean isAdded = DiyGeofence.addGeofence(context, id, lat, lng, rad);
        if (isAdded) {
            mGeofenceManager.registerGeofence(id, lat, lng, rad);
        }
        return isAdded;
    }

    public static boolean isGeofenceAdded(Context context, String id, double lat, double lng, float rad) {
        Set<String> registeredGeofences = SharedPrefsManager.get(context).getStringSet(SharedPrefsManager.REGISTERED_GEOFENCES, new HashSet<String>());
        return registeredGeofences.contains(id) && DiyGeofence.isGeofenceAdded(context, id, lat, lng, rad);
    }

    public static void reRegisterAllGeofences(Context context) {
        if (mGeofenceManager == null) {
            init(context);
        }
        mGeofenceManager.reRegisterAllGeofences(DiyGeofence.getAllGeofences(context));
    }

    public static boolean removeGeofence(Context context, String id) {
        if (mGeofenceManager == null) {
            init(context);
        }

        boolean isRemoved = DiyGeofence.removeGeofence(context, id);
        if (isRemoved) {
            mGeofenceManager.unregisterGeofence(id);
        }
        return isRemoved;
    }

    public static boolean removeAllGeofences(Context context) {
        if (mGeofenceManager == null) {
            init(context);
        }

        boolean areRemoved = DiyGeofence.removeAllGeofences(context);
        if (areRemoved) {
            mGeofenceManager.unregisterAllGeofences();
        }
        return areRemoved;
    }

    public static JSONObject getGeofence(Context context, String id) {
        return DiyGeofence.getGeofence(context, id);
    }

    public static JSONArray getAllGeofences(Context context) {
        return DiyGeofence.getAllGeofences(context);
    }

    public static void setLocation(Context context, Location location) {
        DiyGeofence.setLocation(context, location);
    }

    public static void updateLocation(final Context context, final boolean force) {
        DiyGeofence.updateLocation(context, force);
    }

    // Internal
    static void dispatchEnterCallback(final Context context, final String id) {
        if (mEnhancedGeofenceListener != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mEnhancedGeofenceListener != null) {
                        SharedPrefsManager sharedPrefsManager = SharedPrefsManager.get(context);
                        Set<String> lastEnteredGeofences = sharedPrefsManager.getStringSet(SharedPrefsManager.LAST_ENTERED_GEOFENCES, new HashSet<String>());
                        if (!lastEnteredGeofences.contains(id)) {
                            mEnhancedGeofenceListener.onEnter(context, id);
                            lastEnteredGeofences.add(id);
                            sharedPrefsManager.put(SharedPrefsManager.LAST_ENTERED_GEOFENCES, lastEnteredGeofences);
                        }
                    }
                }
            });
        }
    }

    static void dispatchExitCallback(final Context context, final String id) {
        if (mEnhancedGeofenceListener != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mEnhancedGeofenceListener != null) {
                        SharedPrefsManager sharedPrefsManager = SharedPrefsManager.get(context);
                        Set<String> lastEnteredGeofences = sharedPrefsManager.getStringSet(SharedPrefsManager.LAST_ENTERED_GEOFENCES, new HashSet<String>());
                        if (lastEnteredGeofences.contains(id)) {
                            mEnhancedGeofenceListener.onExit(context, id);
                            lastEnteredGeofences.remove(id);
                            sharedPrefsManager.put(SharedPrefsManager.LAST_ENTERED_GEOFENCES, lastEnteredGeofences);
                        }
                    }
                }
            });
        }
    }
}
