package hasine.project.mprog.run.Activities;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * inspired by http://blog.teamtreehouse.com/beginners-guide-location-android
 *
 * Copyright 2013 Google Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *
 */


import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import hasine.project.mprog.run.R;
import hasine.project.mprog.run.Tools.SphericalUtil;


public class MakeRunActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest mLocationRequest;
    private PolylineOptions rectOptions;
    private double lat_loc, long_loc;
    private String goal;
    private SharedPreferences SP;
    private TextView mTextView;
    private EditText mEditText;
    private int index;
    List<Polyline> polylines = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<LatLng> locations = new ArrayList<>();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MakeRunActivity.class.getSimpleName();
    private double total_distance = 0, close_circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_run);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mTextView = (TextView) findViewById(R.id.textView);
        mEditText = (EditText) findViewById(R.id.editText);
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        client.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor SPEditor = SP.edit();
        SPEditor.putFloat("lat_loc", (float) lat_loc);
        SPEditor.putFloat("long_loc", (float) long_loc);
        SPEditor.putString("goal", goal);
        SPEditor.apply();


        if (client.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
            client.disconnect();
        }
    }

    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(client);
        if (location == null) {
            Log.i(TAG, "Location is null.");
            LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
        }
        handleNewLocation(location);

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));

        lat_loc = location.getLatitude();
        long_loc = location.getLongitude();

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("First Place")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.showInfoWindow();
        markers.add(marker);

        // To make a circuit, I insert at first twice my location
        rectOptions.add(myLocation);
        rectOptions.add(myLocation);
    }

    @Override
    public void onLocationChanged(Location location) {

        handleNewLocation(location);
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        rectOptions = new PolylineOptions();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (markers.get(0).equals(marker)){
            rectOptions.getPoints().remove(markers.size());
        }

        index = markers.lastIndexOf(marker);
        rectOptions.getPoints().remove(index);

        markers.remove(marker);

        for(Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();

        mTextView.setText(R.string.calcRouteTextView);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mTextView.setText(R.string.calcRouteTextView);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        markers.add(index, marker);
        rectOptions.getPoints().add(index, marker.getPosition());
        if (markers.get(0).equals(marker)){
            rectOptions.add(marker.getPosition());
        }

        close_circuit = calcDistance(rectOptions.getPoints().get(markers.size() - 1), markers.get(0)
                .getPosition());
        total_distance = 0;
        for (int i = 0; i < markers.size(); i++){
            total_distance +=
                    calcDistance(rectOptions.getPoints().get(i), rectOptions.getPoints().get(i + 1));
        }

        Polyline polyline = mMap.addPolyline(rectOptions);
        polylines.add(polyline);

        mTextView.setText(getString(R.string.lengthRouteTextView, formatNumber(total_distance)));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //create new marker when user long clicks
        int size = markers.size() + 1;
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title(size + " Place")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.showInfoWindow();
        markers.add(marker);

        // erase the last added line from the polyline which closes the circuit
        for(Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();

        // checks if there is a new starting point marker
        if (markers.size() == 1){
            Toast.makeText(this, "You have chosen a new starting point!", Toast.LENGTH_SHORT).show();
            rectOptions.add(latLng);
            rectOptions.add(latLng);
            lat_loc = marker.getPosition().latitude;
            long_loc = marker.getPosition().longitude;
            total_distance = 0;
            close_circuit = 0;
        }

        // add the coordinates to rectOptions to get the appropriate polyline
        // calculate distance between new set marker and previous one
        for (int i = 0; i < markers.size(); i++){
            if (marker == markers.get(i) && markers.size() > 1){
                total_distance += calcDistance(markers.get(i - 1).getPosition(), markers.get(i)
                        .getPosition());
                rectOptions.getPoints().remove(markers.size() - 1);
                rectOptions.add(latLng);
                rectOptions.getPoints().add(markers.get(0).getPosition());
                total_distance -= close_circuit;
            }
        }

        // add the distance between last marker and starting point
        close_circuit = calcDistance(latLng, markers.get(0).getPosition());
        total_distance += close_circuit;

        Polyline polyline = mMap.addPolyline(rectOptions);
        polylines.add(polyline);

        mTextView.setText(getString(R.string.lengthRouteTextView, formatNumber(total_distance)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.remove();
        markers.remove(marker);

        for(Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();

        rectOptions.getPoints().clear();
        rectOptions = new PolylineOptions();

        for (Marker mar : markers){
            LatLng position = mar.getPosition();
            rectOptions.add(position);
        }

        if (markers.size() != 0){
            rectOptions.add(markers.get(0).getPosition());
            close_circuit = calcDistance(rectOptions.getPoints().get(markers.size() - 1),
                    markers.get(0).getPosition());
        }

        Polyline polyline = mMap.addPolyline(rectOptions);
        polylines.add(polyline);

        total_distance = 0;
        for (int i = 0; i < markers.size(); i++){
            total_distance +=
                    calcDistance(rectOptions.getPoints().get(i), rectOptions.getPoints().get(i + 1));
        }

        mTextView.setText(getString(R.string.lengthRouteTextView, formatNumber(total_distance)));
        return false;
    }

    private double calcDistance(LatLng start, LatLng end) {
        return SphericalUtil.computeDistanceBetween(start, end);
    }

    private String formatNumber(double distance) {
        String unit = "m";
        if (distance > 1000) {
            distance /= 1000;
            unit = "km";
            return String.format("%4.3f%s", distance, unit);
        }
        return String.format("%4.0f%s", distance, unit);
    }

    public void saveGoal(View view) {
        goal = mEditText.getText().toString();

        Toast.makeText(this, R.string.saveGoalToast, Toast.LENGTH_SHORT).show();
    }

    public void saveRoute(View view) {
        Toast.makeText(this, R.string.routeSavedToast, Toast.LENGTH_SHORT).show();

//        // To convert locationsdata to jsonarray
//        JSONObject locationsObj = new JSONObject();
//        JSONArray locationsArray = new JSONArray();
//
//        try {
//            for (int i = 0; i < rectOptions.getPoints().size(); i++) {
//                locationsObj.put("location", rectOptions.getPoints().get(i));
//                locationsArray.put(i, locationsObj);
//            }
//            locationsObj.put("locations", locationsArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        String jsonStr = locationsObj.toString();
//
//        SharedPreferences.Editor SPEditor = SP.edit();
//        SPEditor.putString("locations", jsonStr);
//        SPEditor.apply();

        Intent gotoStart = new Intent(this, StartRunActivity.class);
        startActivityForResult(gotoStart, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(goal, String.valueOf(mEditText.getText()));
        for (int i = 0; i < rectOptions.getPoints().size(); i++){
            locations.add(rectOptions.getPoints().get(i));
        }
        outState.putParcelableArrayList("locations", locations);
        outState.putDouble("total_distance", total_distance);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEditText.setText(goal);

        rectOptions = new PolylineOptions();
        for (int i = 0; i < locations.size(); i++){
            rectOptions.add(locations.get(i));
        }

//        Polyline polyline = mMap.addPolyline(rectOptions);
//        polylines.add(polyline);

        mTextView.setText(getString(R.string.lengthRouteTextView, formatNumber(total_distance)));
    }
}