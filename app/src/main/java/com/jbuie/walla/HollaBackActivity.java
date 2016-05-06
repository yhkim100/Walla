package com.jbuie.walla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HollaBackActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Bundle extras;
    PostRequest myRequest;
    String postUid;
    ConversationClass newConvo;
    private Firebase mFirebasePostRef;
    private Firebase mFirebaseConvoRef;
    private Firebase userAuth;
    String userName;

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holla_back);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_holla_back);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title_holla_back);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


        final TextView hollaRequestTxt = (TextView) findViewById(R.id.holla_back_requested);
        final TextView timeLocDetails = (TextView) findViewById(R.id.timeLocDetails);
        final TextView additionalDetails = (TextView) findViewById(R.id.holla_back_additionalDetails);
        final TextView hollaUserName = (TextView) findViewById(R.id.holla_back_username);

        //Retrieve Request Information
        extras = getIntent().getExtras();
        myRequest = extras.getParcelable("myRequest");
        postUid = extras.getString("postRef");

        if (myRequest != null) {

            hollaRequestTxt.setText(myRequest.getRequest());
            timeLocDetails.setText(myRequest.getTimeStamp());
            additionalDetails.setText(myRequest.getAdditionalDetails());
            hollaUserName.setText(myRequest.getWhoRequested());

        } else {
            hollaRequestTxt.setText("COULD NOT FIND INFO");
            timeLocDetails.setText("COULD NOT FIND INFO");
            additionalDetails.setText("COULD NOT FIND INFO");
            hollaUserName.setText("COULD NOT FIND INFO");
        }

        if (postUid == null) {
            // Not using this right now. leave because may need to restructure data base :(
            Toast.makeText(getApplicationContext(), "Dont have a valid reference", Toast.LENGTH_SHORT).show();
        }

        //mFirebasePostRef = new Firebase("https://amber-torch-611.firebaseio.com/Requests/");
        //mFirebasePostRef  = new Firebase("https://amber-torch-611.firebaseio.com/Dev/Requests/");
        //mFirebaseConvoRef = new Firebase("https://amber-torch-611.firebaseio.com/Dev/Conversations/");
        mFirebaseConvoRef = new Firebase("https://amber-torch-611.firebaseio.com/Conversations/");
        userAuth = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles");


        Button holla_back = (Button) findViewById(R.id.btn_holla_back);
        holla_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (myRequest.getWhoRequested().matches(userName)) {
                    Toast.makeText(getApplicationContext(), "Not creating a convo with yourself", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(getApplicationContext(), "Creating Conversation with" + myRequest.getWhoRequested(), Toast.LENGTH_SHORT).show();
                    newConvo = new ConversationClass(userName, myRequest.getWhoRequested(), postUid);
                    //Firebase tempRef = mFirebaseConvoRef.push().getRef();
                    String convoID = getConvoID(userName, myRequest.getWhoRequested(), myRequest.getRequest()+myRequest.getTimeStamp());
                    Firebase tempRef = mFirebaseConvoRef.child(convoID);
                    /*if (mFirebaseConvoRef.equalTo(convoID) != null){
                        //Go to the convo that already exists
                        Toast.makeText(getApplicationContext(),"Convo already exists",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        tempRef.setValue(newConvo);
                    }*/
                    tempRef.setValue(newConvo);
                    Intent i = new Intent(getApplicationContext(), PrivateMessageActivity.class);
                    i.putExtra("convoID", convoID);
                    startActivity(i);
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        AuthData authData = userAuth.getAuth();
        if (authData == null) {
            startActivity(new Intent(HollaBackActivity.this, LoginActivity.class));
        } else {
            userName = userAuth.getAuth().getProviderData().get("email").toString();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        enableMyLocation();
        double latitude = myRequest.getLatitude();
        double longitude = myRequest.getLongitude();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Request Location")
        );

    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
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
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Toast.makeText(HollaBackActivity.this, "My Location Button Clicked", Toast.LENGTH_SHORT).show();

                    // Return false so that we don't consume the event and the default behavior still occurs
                    // (the camera animates to the user's current position).
                    return false;
                }
            });
        }

    public String getConvoID(String u1, String u2, String tm){
        String t = u1 + u2 + tm;
        return String.valueOf(t.hashCode());
    }
}
