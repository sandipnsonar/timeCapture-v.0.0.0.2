package com.india.timecapture;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private static final String CHANNEL_ID = "location_service_channel";
    private static final int LOCATION_UPDATE_INTERVAL = 1000; // 1 second

    private int locationTimeout;
    private float minDistance;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private long lastUpdateTime;
    private DB_Helper dbHelper;
    private Handler handler;
    private Runnable locationTimeoutRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        dbHelper = new DB_Helper(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest.Builder(LOCATION_UPDATE_INTERVAL) // Request location updates every 1000 milliseconds (1 second)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateDistanceMeters(0) // Minimum displacement to trigger an update
                .build();

        handler = new Handler();
        locationTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                storeCurrentLocation();
                handler.postDelayed(this, locationTimeout);
            }
        };

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                handler.removeCallbacks(locationTimeoutRunnable);

                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "New location: " + location.getLatitude() + ", " + location.getLongitude());

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation != null) {
                        long timestamp = lastKnownLocation.getTime();
                        Log.d(TAG, "GPS Time (ms) : " + timestamp);
                    }
                    //main logic
                    long currentTime = System.currentTimeMillis();
                    if (lastLocation == null || location.distanceTo(lastLocation) >= minDistance || currentTime - lastUpdateTime >= locationTimeout) {
                        lastLocation = location;
                        lastUpdateTime = currentTime;
                        storeLocation(location);
                        Toast.makeText(LocationService.this, "Location has been stored successfully.", Toast.LENGTH_SHORT).show();

                        // Broadcast location update
                        broadcastLocationUpdate();
                    }
                }

                handler.postDelayed(locationTimeoutRunnable, locationTimeout);
            }
        };

        createNotificationChannel();
        startForeground(1, getNotification());

        startLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationTimeout = intent.getIntExtra("locationTimeout", 0);
        minDistance = intent.getFloatExtra("minDistance", 0);
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification getNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Tracking your location in the background")
                .setSmallIcon(R.drawable.ic_location)
                .build();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        handler.postDelayed(locationTimeoutRunnable, locationTimeout);
    }

    private void storeLocation(Location location) {
        Log.d(TAG, "New Timeout: " + locationTimeout + ", New Distance: " +minDistance);
        long gpsTime = location.getTime(); // This should give the GPS time in milliseconds since epoch
        Log.d(TAG, "GPS Time (ms): " + gpsTime);

        String formattedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(gpsTime));
        Log.d(TAG, "Formatted GPS DateTime: " + formattedDateTime);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Storing data in the database
        dbHelper.putData(gpsTime, String.valueOf(latitude), String.valueOf(longitude));
        Log.d(TAG, "Stored data: DateTime: " + gpsTime + ", Latitude: " + latitude + ", Longitude: " + longitude);
    }

    private void storeCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        lastLocation = location;
                        lastUpdateTime = location.getTime(); // Use GPS time
                        storeLocation(location);
                        // Broadcast location update
                        broadcastLocationUpdate();
                    }
                });
    }

    private void broadcastLocationUpdate() {
        Intent intent = new Intent("LOCATION_DATA_UPDATED");
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        handler.removeCallbacks(locationTimeoutRunnable);
    }
}