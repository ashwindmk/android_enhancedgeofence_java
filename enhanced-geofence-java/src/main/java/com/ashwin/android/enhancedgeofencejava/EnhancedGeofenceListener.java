package com.ashwin.android.enhancedgeofencejava;

import android.content.Context;

public interface EnhancedGeofenceListener {
    void onEnter(Context context, String id);
    void onExit(Context context, String id);
}
