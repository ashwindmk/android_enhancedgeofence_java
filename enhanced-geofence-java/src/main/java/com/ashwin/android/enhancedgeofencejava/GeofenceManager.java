package com.ashwin.android.enhancedgeofencejava;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GeofenceManager {
    private static final int GEOFENCE_MAX_LIMIT = 100;

    private static GeofenceManager instance = null;

    private Context mApplicationContext = null;

    private GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent = null;

    private GeofenceManager(Context context) {
        mGeofencingClient = LocationServices.getGeofencingClient(context);
        mApplicationContext = context.getApplicationContext();
    }

    static GeofenceManager get(Context context) {
        if (instance == null) {
            instance = new GeofenceManager(context);
        }
        return instance;
    }

    private boolean hasLocationPermission() {
        PackageManager pm = mApplicationContext.getPackageManager();
        String packageName = mApplicationContext.getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, packageName) == PackageManager.PERMISSION_GRANTED
                    && pm.checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION, packageName) == PackageManager.PERMISSION_GRANTED;
        } else {
            return pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, packageName) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(Context context) {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, EnhancedGeofenceReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    void registerGeofence(String id, double lat, double lng, float rad) {
        if (hasLocationPermission()) {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(id)
                    .setCircularRegion(lat, lng, rad)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            mGeofencingClient.addGeofences(getGeofencingRequest(geofence), getGeofencePendingIntent(mApplicationContext));

            SharedPrefsManager sharedPrefsManager = SharedPrefsManager.get(mApplicationContext);
            Set<String> registeredGeofences = sharedPrefsManager.getStringSet(SharedPrefsManager.REGISTERED_GEOFENCES, new HashSet<String>());
            registeredGeofences.add(id);
            sharedPrefsManager.put(SharedPrefsManager.REGISTERED_GEOFENCES, registeredGeofences);
        }
    }

    void reRegisterAllGeofences(JSONArray jsonArray) {
        unregisterAllGeofences();
        registerAllGeofences(jsonArray);
    }

    private void registerAllGeofences(JSONArray jsonArray) {
        if (jsonArray != null && jsonArray.length() > 0 && hasLocationPermission()) {
            List<Geofence> geofenceList = new ArrayList<>();
            Set<String> geofenceSet = new HashSet<String>();
            int len = jsonArray.length();
            for (int i = 0; i < Math.min(len, GEOFENCE_MAX_LIMIT); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    Geofence geofence = new Geofence.Builder()
                            .setRequestId(id)
                            .setCircularRegion(
                                    jsonObject.getDouble("lat"),
                                    jsonObject.getDouble("lng"),
                                    ((Number) jsonObject.getDouble("rad")).floatValue())
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build();
                    geofenceList.add(geofence);
                    geofenceSet.add(id);
                } catch (JSONException e) {
                    Log.e(EnhancedGeofence.TAG, "Exception while registering geofence", e);
                }
            }

            if (!geofenceList.isEmpty()) {
                GeofencingRequest geofencingRequest = getGeofencingRequest(geofenceList);
                mGeofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent(mApplicationContext));
                SharedPrefsManager.get(mApplicationContext).put(SharedPrefsManager.REGISTERED_GEOFENCES, geofenceSet);
            }
        }
    }

    void unregisterGeofence(String id) {
        if (hasLocationPermission()) {
            List<String> ids = new ArrayList<>();
            ids.add(id);
            mGeofencingClient.removeGeofences(ids);

            SharedPrefsManager sharedPrefsManager = SharedPrefsManager.get(mApplicationContext);
            Set<String> registeredGeofences = sharedPrefsManager.getStringSet(SharedPrefsManager.REGISTERED_GEOFENCES, new HashSet<String>());
            registeredGeofences.remove(id);
            sharedPrefsManager.put(SharedPrefsManager.REGISTERED_GEOFENCES, registeredGeofences);
        }
    }

    void unregisterAllGeofences() {
        if (hasLocationPermission()) {
            mGeofencingClient.removeGeofences(getGeofencePendingIntent(this.mApplicationContext));
            SharedPrefsManager.get(mApplicationContext).put(SharedPrefsManager.REGISTERED_GEOFENCES, null);
        }
    }
}
