package com.ashwin.android.enhancedgeofencejavademo;

import android.app.Application;
import android.util.Log;

import com.ashwin.android.enhancedgeofencejava.EnhancedGeofence;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initEnhancedGeofence();
    }

    private void initEnhancedGeofence() {
        EnhancedGeofence.init(this);
        EnhancedGeofence.setLogLevel(Log.VERBOSE);
        EnhancedGeofence.setAutoLocationUpdates(this, true);
        EnhancedGeofence.setGeofenceListener(new MyGeofenceListener());
    }
}
