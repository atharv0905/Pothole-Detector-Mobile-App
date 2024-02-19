package com.atharv.potholedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Button currentLocationBtn;

    // --------------------------------------------------------------------------------------------------------------------------
    // setting marking to current location
    private void goToCurrentLocation() {
        GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            // creating latlng object
            LatLng latLng = new LatLng(latitude, longitude);

            // creating marker object
            MarkerOptions marker = new MarkerOptions().position(latLng).title("My Current Location");

            // adding marker to map
            mMap.addMarker(marker);

            // moving camera to desired latlng
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // zoom camera to desired latlng
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
    // setting marking to current location
    // --------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // finding map by fragment id
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);

        currentLocationBtn = (Button) findViewById(R.id.currentLocation);
        currentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCurrentLocation();
            }
        });
    }

    @Override // all map related functionalities will be done here
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
}