package com.ashwin.android.enhancedgeofencejava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class EnhancedGeofenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
            for (Geofence geofence : geofences) {
                if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    EnhancedGeofence.dispatchEnterCallback(context, geofence.getRequestId());
                } else {
                    EnhancedGeofence.dispatchExitCallback(context, geofence.getRequestId());
                }
            }
        }
    }
}
