package hasine.project.mprog.run;

// inspired by http://blog.teamtreehouse.com/beginners-guide-location-android
//Copyright 2013 Google Inc.
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

import android.Manifest;
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

public class SearchActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest mLocationRequest;
    private LatLng myLocation;
    private PolylineOptions rectOptions;
    private double lat_loc, long_loc;
    private String goal;
    private SharedPreferences SP;
    private TextView mTextView;
    private EditText mEditText;
    List<Polyline> polylines = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = SearchActivity.class.getSimpleName();
    private double total_distance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

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
        SPEditor.commit();


        if (client.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
            client.disconnect();
        }
    }

    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
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
        else {
            handleNewLocation(location);
        };
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));

        lat_loc = location.getLatitude();
        long_loc = location.getLongitude();
        Log.i(TAG, "Now in handleNewLocation");

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("First Place")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.showInfoWindow();
        markers.add(marker);
        // Instantiates a new Polyline object and adds points to define a rectangle

        rectOptions.add(myLocation);
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
        Log.i(TAG, "Now in onLocationChanged");
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
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
        markers.remove(marker);


    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        markers.add(marker);


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

        for (int i = 0; i < markers.size() - 1; i++){
            total_distance +=
                    calcDistance(markers.get(i).getPosition(), markers.get(i + 1).getPosition());
        }

        if (markers.size() <= 2){
            rectOptions.add(latLng);
        }

        if (markers.size() > 2){
            total_distance -= calcDistance(markers.get(markers.size() - 1).getPosition(), myLocation);
            for(Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();

            if (markers.size() > 3){
                rectOptions.getPoints().remove(markers.size() - 1);
            }
            rectOptions.add(latLng);
            rectOptions.getPoints().add(markers.size(), myLocation);

        }

        total_distance +=  calcDistance(latLng, myLocation);

        Polyline polyline = mMap.addPolyline(rectOptions);
        polylines.add(polyline);

        mTextView.setText("Length route: " + formatNumber(total_distance));
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

        if (markers.size() > 2){
            rectOptions.getPoints().add(markers.size(), myLocation);
        }

        Polyline polyline = mMap.addPolyline(rectOptions);
        polylines.add(polyline);

        total_distance = 0;
        for (int i = 0; i < rectOptions.getPoints().size() - 1; i++){
            total_distance +=
                    calcDistance(rectOptions.getPoints().get(i), rectOptions.getPoints().get(i + 1));
        }
        mTextView.setText("Length route: " + formatNumber(total_distance));
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

    public void savegoal(View view) {
        goal = mEditText.getText().toString();
        Toast.makeText(this, "Goal Saved!", Toast.LENGTH_SHORT).show();
    }
}