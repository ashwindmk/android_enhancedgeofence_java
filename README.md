
# Enhanced Geofence Java

This Android library registers [Geofences](https://developer.android.com/training/location/geofencing) as well as uses [DIY Geofence](https://github.com/ashwindmk/android_diygeofence_java).

[ ![Download](https://api.bintray.com/packages/ashwin-dinesh/maven/enhanced-geofence-java/images/download.svg) ](https://bintray.com/ashwin-dinesh/maven/enhanced-geofence-java/_latestVersion)


### Advantages:

	1. No limit on number of geofences.
	2. Survives device reboots.
	3. Higher probability of receiving geofence transition triggers on time.
	4. Better precision.
	5. Easy implementation.


### Installation

app/build.gradle

```gradle
...
dependencies {
    ...
    implementation 'com.ashwin.android:enhanced-geofence-java:0.0.2'
    implementation 'com.google.android.gms:play-services-location:17.0.0'
}
```


### Permissions

```xml
<manifest ...
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION"/>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
    ...
```

Request `ACCESS_FINE_LOCATION` (for Android version >= 23) and `ACCESS_BACKGROUND_LOCATION` (for Android version >= 29) permissions in your Activity.


### Initialization

```java
public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the SDK
        EnhancedGeofence.init(this);

        ...
    }
}
```


### Configurations

```java
public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ...

        // Set -1 for stop logging
        EnhancedGeofence.setLogLevel(Log.VERBOSE);

        // This will start periodic location updates
        EnhancedGeofence.setAutoLocationUpdates(this, true);

        // You will get geofence on-enter and on-exit callbacks in this instance
        EnhancedGeofence.setGeofenceListener(new YourGeofenceListener());
    }
}
```


### Usage

##### Add geofence:

```java
    String geofenceId = "geofence-id";
    double latitude = 19.0607;
    double longitude = 72.8416;
    float radius = 500f;
    EnhancedGeofence.addGeofence(context, geofenceId, latitude, longitude, radius);
```


##### Remove geofence:

```java
    String geofenceId = "geofence-id";
    EnhancedGeofence.removeGeofence(context, geofenceId);
```


##### Remove all geofences:

```java
    EnhancedGeofence.removeAllGeofences(context);
```


##### Get on-enter and on-exit callbacks on Main thread.

Register EnhancedGeofenceListener implementation class in your Application class:

```java
public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ...

        EnhancedGeofence.setGeofenceListener(new YourGeofenceListener());
    }
}
```


Get on-enter and on-exit callbacks on Main thread.

```java
public class YourGeofenceListener implements EnhancedGeofenceListener {
    @Override
    public void onEnter(Context context, String id) {
        // Entered geofence: ${id}
    }

    @Override
    public void onExit(Context context, String id) {
        // Exited geofence: ${id}
    }
}
```
