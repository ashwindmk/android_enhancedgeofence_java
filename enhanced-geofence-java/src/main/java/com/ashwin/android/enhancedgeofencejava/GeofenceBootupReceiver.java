package com.ashwin.android.enhancedgeofencejava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GeofenceBootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EnhancedGeofence.reRegisterAllGeofences(context);
    }
}
