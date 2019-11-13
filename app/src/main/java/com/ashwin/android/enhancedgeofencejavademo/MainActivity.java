package com.ashwin.android.enhancedgeofencejavademo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ashwin.android.enhancedgeofencejava.EnhancedGeofence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = EnhancedGeofence.TAG + ": app";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String GEOFENCES_ADDED_KEY = "GEOFENCES_ADDED";

    private Button mGeofencesButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGeofencesButton = (Button) findViewById(R.id.geofences_button);

        mGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean areGeofencesAdded = getGeofencesAdded(getApplicationContext());
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    if (areGeofencesAdded) {
                        removeGeofences();
                    } else {
                        addGeofences();
                    }
                }
            }
        });

        setButtonState();
    }

    static boolean getGeofencesAdded(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(GEOFENCES_ADDED_KEY, false);
    }

    private void updateGeofencesAdded(Context context, boolean added) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(GEOFENCES_ADDED_KEY, added)
                .apply();

        setButtonState();
    }

    private void setButtonState() {
        if (getGeofencesAdded(getApplicationContext())) {
            mGeofencesButton.setText("REMOVE");
        } else {
            mGeofencesButton.setText("ADD");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAddedGeofences();
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        } else {
            return checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private boolean checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "on-request-permission-results: " + Arrays.toString(grantResults));
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
                Toast.makeText(MainActivity.this, "Permission cancelled", Toast.LENGTH_LONG).show();
            } else {
                boolean allPermissionsGranted = false;

                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = true;
                    } else {
                        allPermissionsGranted = false;
                        break;
                    }
                }

                if (allPermissionsGranted) {
                    Log.i(TAG, "All permissions granted");
                    Toast.makeText(MainActivity.this, "All permissions granted", Toast.LENGTH_LONG).show();
                    addGeofences();
                } else {
                    Log.i(TAG, "Insufficient permission");
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void addGeofences() {
        Log.w(TAG, "Adding geofences...");

        Context context = getBaseContext();
        float rad = 500f;
        EnhancedGeofence.addGeofence(context, "bandra", 19.0607, 72.8416, rad);
        EnhancedGeofence.addGeofence(context, "vakola", 19.0816, 72.8556, rad);
        EnhancedGeofence.addGeofence(context, "santacruz", 19.0817, 72.8415, rad);
        EnhancedGeofence.addGeofence(context, "vile_parle", 19.0995, 72.8439, rad);
        EnhancedGeofence.addGeofence(context, "andheri", 19.1189, 72.8472, rad);
        EnhancedGeofence.addGeofence(context, "jogeshwari", 19.1361, 72.8488, rad);
        EnhancedGeofence.addGeofence(context, "ram_mandir", 19.1516, 72.8501, rad);
        EnhancedGeofence.addGeofence(context, "lotus_corporate_park", 19.1447, 72.8533, rad);
        EnhancedGeofence.addGeofence(context, "goregaon", 19.1648, 72.8493, rad);

        updateGeofencesAdded(getApplicationContext(), true);
    }

    private void getAddedGeofences() {
        JSONArray geofences = EnhancedGeofence.getAllGeofences(getBaseContext());
        int len = geofences.length();
        Log.w(TAG, "registered geofences: " + len);
        for (int i = 0; i < len; i++) {
            try {
                JSONObject geofence = geofences.getJSONObject(i);
                Log.w(TAG, String.valueOf(geofence));
            } catch (JSONException e) {
                Log.e(TAG, "Exception while logging geofence", e);
            }
        }
    }

    private void removeGeofences() {
        EnhancedGeofence.removeAllGeofences(getApplicationContext());
        updateGeofencesAdded(getApplicationContext(), false);
    }
}
