//package com.india.timecapture;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.text.format.DateFormat;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//import androidx.work.WorkManager;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//public class LocationWorker extends Worker {
//
//    private static final String TAG = "LocationWorker";
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private DB_Helper dbHelper;
//
//    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//        dbHelper = new DB_Helper(context);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(location -> {
//                        if (location != null) {
//                            String currentDateTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
//                            String latitude = String.valueOf(location.getLatitude());
//                            String longitude = String.valueOf(location.getLongitude());
//                            dbHelper.putData(currentDateTime, latitude, longitude);
//                            Log.d(TAG, "Stored data: DateTime: " + currentDateTime + ", Latitude: " + latitude + ", Longitude: " + longitude);
//                        }
//                    });
//            scheduleNextUpdate();
//            return Result.success();
//        } else {
//            return Result.failure();
//        }
//    }
//
//    private void scheduleNextUpdate() {
//        OneTimeWorkRequest locationWorkRequest = new OneTimeWorkRequest.Builder(LocationWorker.class)
//                .setInitialDelay(30, TimeUnit.SECONDS)
//                .build();
//        WorkManager.getInstance(getApplicationContext()).enqueue(locationWorkRequest);
//    }
//}
//
//
//
//
//
////package com.india.timecapture;
////
////import android.content.Context;
////import android.content.pm.PackageManager;
////import android.location.Location;
////import android.text.format.DateFormat;
////import android.util.Log;
////
////import androidx.annotation.NonNull;
////import androidx.core.content.ContextCompat;
////import androidx.work.OneTimeWorkRequest;
////import androidx.work.Worker;
////import androidx.work.WorkerParameters;
////import androidx.work.WorkManager;
////
////import com.google.android.gms.location.FusedLocationProviderClient;
////import com.google.android.gms.location.LocationServices;
////import com.google.android.gms.tasks.OnSuccessListener;
////
////import java.util.Date;
////import java.util.concurrent.TimeUnit;
////
////public class LocationWorker extends Worker {
////
////    private static final String TAG = "LocationWorker";
////    private FusedLocationProviderClient fusedLocationProviderClient;
////    private DB_Helper dbHelper;
////
////    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
////        super(context, workerParams);
////        dbHelper = new DB_Helper(context);
////        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
////    }
////
////    @NonNull
////    @Override
////    public Result doWork() {
////
////        //ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
////        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////            fusedLocationProviderClient.getLastLocation()
////                    .addOnSuccessListener(new OnSuccessListener<Location>() {
////                        @Override
////                        public void onSuccess(Location location) {
////                            if (location != null) {
////                                String currentDateTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
////                                String latitude = String.valueOf(location.getLatitude());
////                                String longitude = String.valueOf(location.getLongitude());
////                                dbHelper.putData(currentDateTime, latitude, longitude);
////                                Log.d(TAG, "Stored data: DateTime: " + currentDateTime + ", Latitude: " + latitude + ", Longitude: " + longitude);
////                            }
////                        }
////                    });
////            scheduleNextUpdate();
////            return Result.success();
////        } else {
////            return Result.failure();
////        }
////    }
////
////    private void scheduleNextUpdate() {
////        OneTimeWorkRequest locationWorkRequest = new OneTimeWorkRequest.Builder(LocationWorker.class)
////                .setInitialDelay(30, TimeUnit.SECONDS)
////                .build();
////        WorkManager.getInstance(getApplicationContext()).enqueue(locationWorkRequest);
////    }
////}
