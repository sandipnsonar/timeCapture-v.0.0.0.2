package com.india.timecapture;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewDatesActivity extends AppCompatActivity {

    private ListView listView;
    private DB_Helper dbHelper;
    private static final String TAG = "ViewDatesActivity";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dates);

        listView = findViewById(R.id.listView);
        dbHelper = new DB_Helper(this);

        // Show a toast message
        Toast.makeText(ViewDatesActivity.this, "Hello! Welcome to the second page", Toast.LENGTH_SHORT).show();

        // Load and display the dates
        loadDates();

        // Use a handler to execute the loadDates method every 30 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDates();
                handler.postDelayed(this, 30000); // 1 minute
            }
        }, 30000); // 1 minute
    }

    private void loadDates() {
        ArrayList<HashMap<String, String>> dataList = dbHelper.getData();

        for (HashMap<String, String> data : dataList) {
            String dateTime = data.get(DB_Helper.COLUMN_DATETIME);
            String latitude = data.get(DB_Helper.COLUMN_LATITUDE);
            String longitude = data.get(DB_Helper.COLUMN_LONGITUDE);
            Log.d(TAG, "Fetched Data: DateTime: " + dateTime + ", Latitude: " + latitude + ", Longitude: " + longitude);
        }

        CustomAdapter adapter = new CustomAdapter(this, dataList);
        listView.setAdapter(adapter);
    }
}