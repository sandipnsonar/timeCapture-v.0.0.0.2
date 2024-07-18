package com.india.timecapture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            checkBackgroundLocationPermission();
        }

        Button btnViewDates = findViewById(R.id.btnViewDates);
        btnViewDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ViewDatesActivity.class);
                startActivity(intent);
            }
        });

        Button btnViewMap = findViewById(R.id.btnViewMap);
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                startLocationService();
            }
        } else {
            startLocationService();
        }
    }

    private void startLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkBackgroundLocationPermission();
                Toast.makeText(MainActivity.this, "Location permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
                Toast.makeText(MainActivity.this, "Background location permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Background location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}






//package com.india.timecapture;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationRequest.Builder;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;
//
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
//    private Location lastLocation;
//    private DB_Helper dbHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        dbHelper = new DB_Helper(this);
//
//        locationRequest = new Builder(1000) // Request location updates every 1000 milliseconds (1 second)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setMinUpdateDistanceMeters(0) // Minimum displacement to trigger an update
//                .build();
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//
//                for (Location location : locationResult.getLocations()) {
//                    Log.d(TAG, "New location: " + location.getLatitude() + ", " + location.getLongitude());
//
//                    // Check if the location has moved at least 1 meter
//                    if (lastLocation == null || lastLocation.distanceTo(location) >= 50) {
//                        lastLocation = location;
//
//                        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//                        dbHelper.putData(currentDateTime, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
//                        Log.d(TAG, "Stored data: DateTime: " + currentDateTime + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
//
//                        Toast.makeText(MainActivity.this, "Location stored successfully.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        };
//
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            checkBackgroundLocationPermission();
//        }
//
//        Button btnViewDates = findViewById(R.id.btnViewDates);
//        btnViewDates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, ViewDatesActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Button btnViewMap = findViewById(R.id.btnViewMap);
//        btnViewMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//    }
//
//    private void checkBackgroundLocationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
//            } else {
//                startLocationUpdates();
//            }
//        } else {
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                checkBackgroundLocationPermission();
//                Toast.makeText(MainActivity.this, "Location permission granted.", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.w(TAG, "Location permission not granted.");
//                Toast.makeText(MainActivity.this, "Location permission denied.", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startLocationUpdates();
//                Toast.makeText(MainActivity.this, "Background location permission granted.", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.w(TAG, "Background location permission not granted.");
//                Toast.makeText(MainActivity.this, "Background location permission denied.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startLocationUpdates();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
//}
