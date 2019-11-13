-dontpreverify

-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Deprecated
-keepattributes EnclosingMethod
-keepparameternames

-keeppackagenames com.ashwin.android.enhancedgeofencejava

-keep public class * extends android.content.BroadcastReceiver

-keep public class com.ashwin.android.enhancedgeofencejava.EnhancedGeofence
-keep class com.ashwin.android.enhancedgeofencejava.EnhancedGeofence { public <methods>; }
-keepclassmembers class com.ashwin.android.enhancedgeofencejava.EnhancedGeofence {
    public static final java.lang.String TAG;
}

-keep class com.ashwin.android.enhancedgeofencejava.EnhancedGeofenceListener { *; }
