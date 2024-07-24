package com.india.timecapture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;

    private Spinner spinnerTimeout;
    private Spinner spinnerDistance;
    private Button btnSetValues;
    private TextView selectedTimeout;
    private TextView selectedDistance;
    private static final int MINUTES_TO_MILLISECONDS = 60 * 1000;

    private int locationTimeout = 0; // Initialize with 0, will be set later
    private float minDistance = 0; // Initialize with 0, will be set later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerTimeout = findViewById(R.id.spinnerTimeout);
        spinnerDistance = findViewById(R.id.spinnerDistance);
        btnSetValues = findViewById(R.id.btnSetValues);
        selectedTimeout = findViewById(R.id.selectedTimeout);
        selectedDistance = findViewById(R.id.selectedDistance);

        // Set the adapters for the spinners
        ArrayAdapter<CharSequence> timeoutAdapter = ArrayAdapter.createFromResource(this, R.array.timeout_intervals, android.R.layout.simple_spinner_item);
        timeoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeout.setAdapter(timeoutAdapter);

        ArrayAdapter<CharSequence> distanceAdapter = ArrayAdapter.createFromResource(this, R.array.distance_intervals, android.R.layout.simple_spinner_item);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistance.setAdapter(distanceAdapter);

        // Set the default values in the spinners
        spinnerTimeout.setSelection(1); // 3 minutes
        spinnerDistance.setSelection(2); // 50 meters

        // Set the onItemSelectedListener for the spinners
        //for timeout
        spinnerTimeout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String timeoutString = (String) parent.getItemAtPosition(position);
                locationTimeout = convertMinutesToMilliseconds(Integer.parseInt(timeoutString));
                selectedTimeout.setText("Selected Timeout: " + timeoutString + " minutes");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        //for distance
        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String distanceString = (String) parent.getItemAtPosition(position);
                minDistance = Float.parseFloat(distanceString);
                selectedDistance.setText("Selected Distance: " + distanceString + " meters");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set the onClickListener for the button
        btnSetValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the location service method with the selected values
                startLocationService(locationTimeout, minDistance);
                Toast.makeText(MainActivity.this, "Location service started. Timeout: " + locationTimeout + " and Distance: " + minDistance, Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            checkBackgroundLocationPermission();
        }

        Button btnViewDates = findViewById(R.id.btnViewDates);
        btnViewDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
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
                startLocationService(locationTimeout, minDistance);
            }
        } else {
            startLocationService(locationTimeout, minDistance);
        }
    }

    private void startLocationService(int locationTimeout, float minDistance) {
        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("locationTimeout", locationTimeout);
        intent.putExtra("minDistance", minDistance);
        startService(intent);
        // Log.d(TAG, "New Timeout: " + locationTimeout + ", New Distance: " + minDistance);
    }

    private int convertMinutesToMilliseconds(int minutes) {
        return minutes * MINUTES_TO_MILLISECONDS;
    }
}





//package com.india.timecapture;
//
//import static android.content.ContentValues.TAG;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class MainActivity extends AppCompatActivity {
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;
//
//    private Spinner spinnerTimeout;
//    private Spinner spinnerDistance;
//    private Button btnSetValues;
//    private static final int MINUTES_TO_MILLISECONDS = 60 * 1000;
//
//    private int locationTimeout = 0; // Initialize with 0, will be set later
//    private float minDistance = 0; // Initialize with 0, will be set later
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        spinnerTimeout = findViewById(R.id.spinnerTimeout);
//        spinnerDistance = findViewById(R.id.spinnerDistance);
//        btnSetValues = findViewById(R.id.btnSetValues);
//
//        // Set the adapters for the spinners
//        ArrayAdapter<CharSequence> timeoutAdapter = ArrayAdapter.createFromResource(this, R.array.timeout_intervals, android.R.layout.simple_spinner_item);
//        timeoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTimeout.setAdapter(timeoutAdapter);
//
//        ArrayAdapter<CharSequence> distanceAdapter = ArrayAdapter.createFromResource(this, R.array.distance_intervals, android.R.layout.simple_spinner_item);
//        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDistance.setAdapter(distanceAdapter);
//
//        // Set the default values in the spinners
//        spinnerTimeout.setSelection(1); // 3 minutes
//        spinnerDistance.setSelection(2); // 50 meters
//
//        // Set the onItemSelectedListener for the spinners
//        spinnerTimeout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String timeoutString = (String) parent.getItemAtPosition(position);
//                locationTimeout = convertMinutesToMilliseconds(Integer.parseInt(timeoutString));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Do nothing
//            }
//        });
//
//        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String distanceString = (String) parent.getItemAtPosition(position);
//                minDistance = Float.parseFloat(distanceString);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Do nothing
//            }
//        });
//
//        // Set the onClickListener for the button
//        btnSetValues.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Call the location service method with the selected values
//                startLocationService(locationTimeout, minDistance);
//            }
//        });
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
//    private void checkBackgroundLocationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
//            } else {
//                startLocationService(locationTimeout, minDistance);
//            }
//        } else {
//            startLocationService(locationTimeout, minDistance);
//        }
//    }
//
//    private void startLocationService(int locationTimeout, float minDistance) {
//        Intent intent = new Intent(this, LocationService.class);
//        intent.putExtra("locationTimeout", locationTimeout);
//        intent.putExtra("minDistance", minDistance);
//        startService(intent);
//        //Log.d(TAG, "New Timeout: " + locationTimeout + ", New Distance: " +minDistance);
//    }
//
//    private int convertMinutesToMilliseconds(int minutes) {
//        return minutes * MINUTES_TO_MILLISECONDS;
//    }
//}