package com.ashwin.android.enhancedgeofencejavademo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ashwin.android.enhancedgeofencejava.EnhancedGeofence;
import com.ashwin.android.enhancedgeofencejava.EnhancedGeofenceListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyGeofenceListener implements EnhancedGeofenceListener {
    private static final String CHANNEL_ID = "my-channel";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    @Override
    public void onEnter(Context context, String id) {
        Log.w(EnhancedGeofence.TAG, "app: on-enter: " + id);
        String title = formatter.format(new Date()) + ": Entered";
        showNotification(context, title, id);
    }

    @Override
    public void onExit(Context context, String id) {
        Log.w(EnhancedGeofence.TAG, "app: on-exit: " + id);
        String title = formatter.format(new Date()) + ": Exited";
        showNotification(context, title, id);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my-channel";
            String description = "my-channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Context context, String title, String msg) {
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((title + msg).hashCode(), builder.build());
    }
}
