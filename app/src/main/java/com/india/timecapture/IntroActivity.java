package com.india.timecapture;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class IntroActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Spinner spinnerTimeout;
    private Spinner spinnerDistance;
    private Button btnSetValues;
    private static final int MINUTES_TO_MILLISECONDS = 60 * 1000;

    private int locationTimeout = 0; // Initialize with 0, will be set later
    private float minDistance = 0; // Initialize with 0, will be set later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initialize the UI components
        spinnerTimeout = findViewById(R.id.spinnerTimeout);
        spinnerDistance = findViewById(R.id.spinnerDistance);
        btnSetValues = findViewById(R.id.btnSetValues);

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
        spinnerTimeout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String timeoutString = (String) parent.getItemAtPosition(position);
                locationTimeout = convertMinutesToMilliseconds(Integer.parseInt(timeoutString));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String distanceString = (String) parent.getItemAtPosition(position);
                minDistance = Float.parseFloat(distanceString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set the onClickListener for the button
        btnSetValues.setOnClickListener(v -> {
            // Save the selected values in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("location_timeout", locationTimeout);
            editor.putFloat("min_distance", minDistance);
            editor.putBoolean("intro_shown", true);
            editor.apply();

            // Check for location permissions
            if (ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // If permissions are already granted, proceed to MainActivity
                navigateToMainActivity();
            }
        });
    }

    private int convertMinutesToMilliseconds(int minutes) {
        return minutes * 60 * 1000;
    }

    private void navigateToMainActivity() {
        // Start MainActivity
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, proceed to MainActivity
                navigateToMainActivity();
            } else {
                Toast.makeText(this, "Location permissions are required to proceed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




//package com.india.timecapture;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class IntroActivity extends AppCompatActivity {
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
//        setContentView(R.layout.activity_intro);
//
//        // Initialize the UI components
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
//        btnSetValues.setOnClickListener(v -> {
//            // Save the selected values in SharedPreferences
//            SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putInt("location_timeout", locationTimeout);
//            editor.putFloat("min_distance", minDistance);
//            editor.putBoolean("intro_shown", true);
//            editor.apply();
//
//            // Start MainActivity
//            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        });
//    }
//
//    private int convertMinutesToMilliseconds(int minutes) {
//        return minutes * 60 * 1000;
//    }
//}
