package com.atharv.potholedetector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PotholeActivity extends AppCompatActivity {

    // Variable Declaration
    private final int CAMERA_REQUEST_CODE = 100;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 300;
    ImageView image;
    Bitmap imageBitmap;
    Button openCamera;
    double latitude = 0, longitude = 0;

    // --------------------------------------------------------------------------------------------------------------------------
    // getting location
    private void getLocationAndSendToServer() {
        GPSTracker gpsTracker = new GPSTracker(PotholeActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
    // getting location

    // check for permission and fetch location
    private void fetchLocation(){
        if (ContextCompat.checkSelfPermission(PotholeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PotholeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocationAndSendToServer();
        }
    }
    // check for permission and fetch location
    // --------------------------------------------------------------------------------------------------------------------------


    // --------------------------------------------------------------------------------------------------------------------------
    // activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole);

        image = (ImageView) findViewById(R.id.roadImage);
        openCamera = (Button) findViewById(R.id.openCamera);

        // Opening camera for taking picture
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera, CAMERA_REQUEST_CODE);
            }
        });
    }
    // activity
    // --------------------------------------------------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------------------------------------------------
    // intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) { // Getting data from camera intent
                case CAMERA_REQUEST_CODE:
                    imageBitmap = (Bitmap) (data.getExtras().get("data"));
                    image.setImageBitmap(imageBitmap); // setting image on image view
                    fetchLocation();
                    new PredictPothole().execute(imageBitmap); // predicting image
                    new AddPotholeData().uploadImage(imageBitmap, Double.toString(latitude), Double.toString(longitude), "http://4.247.162.44:3000/pothole/addPothole");
                    break;
            }
        }
    }
    // intent
    // --------------------------------------------------------------------------------------------------------------------------
}
