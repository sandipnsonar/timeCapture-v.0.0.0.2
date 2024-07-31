package com.india.timecapture;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.os.Handler;
import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private DB_Helper dbHelper;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        dbHelper = new DB_Helper(this);

        // Load initial data
        ArrayList<HashMap<String, String>> dataList = dbHelper.getData();
        if (!dataList.isEmpty()) {
            HashMap<String, String> firstLocation = dataList.get(0);
            double latitude = Double.parseDouble(firstLocation.get(DB_Helper.COLUMN_LATITUDE));
            double longitude = Double.parseDouble(firstLocation.get(DB_Helper.COLUMN_LONGITUDE));

            IMapController mapController = mapView.getController();
            GeoPoint startPoint = new GeoPoint(latitude, longitude);
            mapController.setZoom(15.0);
            mapController.setCenter(startPoint);
        }

        loadMarkers();

        // using handler to execute the loadMarkers method every 30 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMarkers();
                handler.postDelayed(this, 60000); // 3 minutes
            }
        }, 60000); // 3 minutes
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void loadMarkers() {
        ArrayList<HashMap<String, String>> dataList = dbHelper.getData();
        for (HashMap<String, String> data : dataList) {
            double latitude = Double.parseDouble(data.get(DB_Helper.COLUMN_LATITUDE));
            double longitude = Double.parseDouble(data.get(DB_Helper.COLUMN_LONGITUDE));
            String dateTime = data.get(DB_Helper.COLUMN_DATETIME);

            GeoPoint point = new GeoPoint(latitude, longitude);
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // Modified anchor
            marker.setTitle("Current Location"); // Add a title to the marker
            marker.setSubDescription("Date: " + dateTime + ", Lat: " + latitude + ", Lon: " + longitude); // Add a subtitle to the marker
            mapView.getOverlays().add(marker);

            // Check if the new point is outside the current view
            if (!isPointInView(point)) {
                IMapController mapController = mapView.getController();
                mapController.setCenter(point); // Center the map on the new point
            }
        }
        mapView.invalidate(); // Invalidate the map view to redraw the markers
    }

        private boolean isPointInView(GeoPoint point) {
        GeoPoint mapCenter = (GeoPoint) mapView.getMapCenter();
        double spanLat = mapView.getBoundingBox().getLatitudeSpan();
        double spanLon = mapView.getBoundingBox().getLongitudeSpan();

        double north = mapCenter.getLatitude() + spanLat / 2;
        double south = mapCenter.getLatitude() - spanLat / 2;
        double east = mapCenter.getLongitude() + spanLon / 2;
        double west = mapCenter.getLongitude() - spanLon / 2;

        return point.getLatitude() <= north && point.getLatitude() >= south &&
                point.getLongitude() <= east && point.getLongitude() >= west;
    }
}