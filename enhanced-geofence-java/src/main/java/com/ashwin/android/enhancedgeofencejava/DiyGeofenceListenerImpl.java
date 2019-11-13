package com.ashwin.android.enhancedgeofencejava;

import android.content.Context;

import com.ashwin.android.diygeofencejava.DiyGeofenceListener;

public class DiyGeofenceListenerImpl implements DiyGeofenceListener {
    @Override
    public void onEnter(Context context, String s) {
        EnhancedGeofence.dispatchEnterCallback(context, s);
    }

    @Override
    public void onExit(Context context, String s) {
        EnhancedGeofence.dispatchExitCallback(context, s);
    }
}
