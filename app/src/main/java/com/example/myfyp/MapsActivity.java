package com.example.myfyp;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.covid19statistics, this);
            layer.addLayerToMap();


            layer.setOnFeatureClickListener(new Layer.OnFeatureClickListener()
            {
                @Override
                public void onFeatureClick(Feature feature) {
                    // Toast.makeText(MapsActivity.this,"Feature Clicked"+ feature.getProperty("CountyName"),Toast.LENGTH_LONG).show();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
                    builder1.setMessage("County Selected: " + feature.getProperty("CountyName") + "\n" + "Current Confirmed Cases: " + feature.getProperty("ConfirmedCovidCases")+ "\n" + "Last Updated: " + feature.getProperty("TimeStampDate"));
                    builder1.setCancelable(true);

                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }











        // Add a marker in Sydney and move the camera

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (mMap !=null) {
            int permission = ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
            else {requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);}}
        mMap.setMyLocationEnabled(true);// use with explicit checking permissio
    }
}