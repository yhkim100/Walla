package com.jbuie.walla;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/* Combination of MAPACTIVITY by Young-hoon Kim merged in the PostRequestActivity by
Jonathan Buie
4/8/2016
 */

public class PostRequestActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Firebase mFirebaseRef;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    String userName;
    private Firebase currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_holla);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title_holla);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //mFirebaseRef = new Firebase("https://amber-torch-611.firebaseio.com/Dev/Requests/");
        mFirebaseRef = new Firebase("https://amber-torch-611.firebaseio.com/Requests/");

        currUser = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles/");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //create Google API Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mapFragment.getMapAsync(this);




        final EditText hollaRequestTxt = (EditText) findViewById(R.id.holla_request);
        final EditText hollaAdditionTxt = (EditText) findViewById(R.id.holla_additional);
        final EditText hollaLocationTxt = (EditText) findViewById(R.id.holla_location);
        /*EditText hollaTagsTxt = (EditText) findViewById(R.id.holla_tags);
            This may become more than a text field. we're gonna want autocompletion
            as well as pulling option (from database?) to uniformly assign tags
            to a post. For now everything is seen by everyone.
        */
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.postRequest:

                final EditText hollaRequestTxt = (EditText) findViewById(R.id.holla_request);
                final EditText hollaAdditionTxt = (EditText) findViewById(R.id.holla_additional);
                final EditText hollaLocationTxt = (EditText) findViewById(R.id.holla_location);

                String requestTxt = hollaRequestTxt.getText().toString();
                String additionTxt = hollaAdditionTxt.getText().toString();
                String locationTxt = hollaLocationTxt.getText().toString();
                if(requestTxt.matches("")){
                    Toast.makeText(getApplicationContext(),"Bruh insert a request",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(locationTxt.matches("")){
                    Toast.makeText(getApplicationContext(),"Location please :)",Toast.LENGTH_SHORT).show();
                    break;
                }
                //OBTAIN MAP COORDINATES
                if (ContextCompat.checkSelfPermission(PostRequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission to access the location is missing.
                    PermissionUtils.requestPermission(PostRequestActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                            Manifest.permission.ACCESS_FINE_LOCATION, true);
                }
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                //OBTAIN TIMESTAMP
                String timeStamp = getTimeStamp();

                //Create the Unique ID
                String uid = getUniqueID(userName,requestTxt,timeStamp);

                if(mCurrentLocation!= null) {
                    PostRequest postReq = new PostRequest(requestTxt, userName, additionTxt, locationTxt, mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude(), timeStamp);
                    mFirebaseRef.child(uid).setValue(postReq);
                }
                else{
                    PostRequest postReq = new PostRequest(requestTxt, userName, additionTxt, locationTxt, 0.0, 0.0, timeStamp);
                    mFirebaseRef.child(uid).setValue(postReq);
                }
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Toast.makeText(PostRequestActivity.this,"My Location Button Clicked", Toast.LENGTH_SHORT).show();

                    if (ContextCompat.checkSelfPermission(PostRequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission to access the location is missing.
                        PermissionUtils.requestPermission(PostRequestActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                                Manifest.permission.ACCESS_FINE_LOCATION, true);
                    }
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mCurrentLocation != null){
                        mMap.clear();
                        double latitude = mCurrentLocation.getLatitude();
                        double longitude = mCurrentLocation.getLongitude();

                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        // Zoom in the Google Map
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));
                    }


                    // Return false so that we don't consume the event and the default behavior still occurs
                    // (the camera animates to the user's current position).
                    return false;
                }
            });
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        userName = currUser.getAuth().getProviderData().get("email").toString();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null){
            mMap.clear();
            double latitude = mCurrentLocation.getLatitude();
            double longitude = mCurrentLocation.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));
        }
    }

    private String getTimeStamp(){
        return new SimpleDateFormat("MM/dd/yy HH:mm").format(new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_request, menu);
        return true;
    }

    public String getUniqueID(String u1, String u2, String tm){
        String t = u1 + u2 + tm;
        return String.valueOf(t.hashCode());
    }
}
