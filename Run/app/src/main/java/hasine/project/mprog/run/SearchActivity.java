package hasine.project.mprog.run;

// inspired by http://blog.teamtreehouse.com/beginners-guide-location-android

import android.Manifest;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
import java.util.ListIterator;

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
    private SharedPreferences SP;
    private Polyline polyline;
    private List<LatLng> rectOptions_points;
    private ArrayList<Marker> markers = new ArrayList<>();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = SearchActivity.class.getSimpleName();

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
        Log.d(TAG, "location.lat in onPause: " + lat_loc);
        Log.d(TAG, "location.long in onPause: " + long_loc);
        SharedPreferences.Editor SPEditor = SP.edit();
        SPEditor.putFloat("lat_loc", (float) lat_loc);
        SPEditor.putFloat("long_loc", (float) long_loc);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

        lat_loc = location.getLatitude();
        long_loc = location.getLongitude();
        Log.i(TAG, "Now in handleNewLocation");

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("First Position")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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

        LatLng pos = marker.getPosition();
        rectOptions_points = rectOptions.getPoints();
        for (int i = 0; i < rectOptions_points.size(); i++){
            if (pos.equals(rectOptions_points.get(i))){
                rectOptions_points.remove(pos);
            }
        }

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        markers.add(marker);

        LatLng pos = marker.getPosition();
        rectOptions_points.add(pos);
        polyline.setPoints(rectOptions_points);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //create new marker when user long clicks
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        markers.add(marker);

        rectOptions.add(latLng).geodesic(true);
        polyline = mMap.addPolyline(rectOptions);


        markers.get(0).getPosition();
        markers.size();

        Log.i(TAG, "arraylist markers: " + markers);
        Log.i(TAG, "rectoptions points" + rectOptions.getPoints());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.remove();
        markers.remove(marker);

        LatLng pos = marker.getPosition();
        List<LatLng> rectOptions_points = rectOptions.getPoints();
        for (int i = 0; i < rectOptions_points.size(); i++){
            if (pos.equals(rectOptions_points.get(i))){
                rectOptions_points.remove(pos);
            }
        }
        polyline.setVisible(false);
        polyline.setPoints(rectOptions_points);
//        polyline.setVisible(true);
        return false;
    }
}
