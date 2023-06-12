package com.sid.androsid.ecodrive.Directions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import com.sid.androsid.ecodrive.Chat.Chats;
import com.sid.androsid.ecodrive.Chat.UserDetails;
import com.sid.androsid.ecodrive.Chat.users;
import com.sid.androsid.ecodrive.LocationSharing.UserLocation;
import com.sid.androsid.ecodrive.R;
import com.sid.androsid.ecodrive.User;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

/**
 * Created by sid on 2019-02-21.
 */

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,
        AdapterView.OnItemSelectedListener

{
    LatLng currentLatLng,destinationLatLng;
    TextView mTextView ;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    double end_latitude, end_longitude;
    String s;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String[] arraySpinner = new String[] {
                "Select Vehicle" , "Small Petrol Car (1.4L)" , "Medium Petrol Car (1.4 to 2.11L)" , "Large Petrol Car" , "Small Diesel Car(up to 1.4L)" ,"Medium Diesel Car(1.4 to 2L)", "Large Diesel Car(More than 2L)" , "Bus"
        };
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

         s = FirebaseAuth.getInstance().getCurrentUser().getEmail();
         s=s.substring(0,s.indexOf('@'));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                UserDetails.chatWith = marker.getTitle();
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity1.this);
                builder.setMessage("Chat with " + marker.getTitle() + " ?").setCancelable(true)
                        .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(MapsActivity1.this, Chats.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Chat?");
                alert.show();

                return true;
            }
        });


        //todo-------------------------------------------------------
    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
}

    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[2];
        GetNearbuyPlacesData getNearbuyPlacesData =new GetNearbuyPlacesData();

        UserLocation userLocation = new UserLocation();
        try {

        switch(v.getId()) {
            case R.id.B_search:

                EditText tf_location = (EditText) findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList = null;
                final MarkerOptions markerOptions = new MarkerOptions();
                Log.d("location = ", location);

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            end_latitude = myAddress.getLatitude();
                            end_longitude = myAddress.getLongitude();
                            markerOptions.position(latLng);
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            destinationLatLng=latLng;

        Log.d("end_lat",""+end_latitude);
        Log.d("end_lng",""+end_longitude);
                        }
                    }

                }

            break;
            case R.id.B_hospital:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);

                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbuyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity1.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
                break;

            case R.id.B_bus:
                mMap.clear();
                dataTransfer = new Object[2];
                String restaurant = "bus";
                url = getUrl(latitude, longitude, restaurant);
                getNearbuyPlacesData = new GetNearbuyPlacesData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                Log.d("My bus url",url);
                getNearbuyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity1.this, "Showing Nearby transport", Toast.LENGTH_LONG).show();
                break;
            case R.id.B_petrol:
                mMap.clear();
                String school = "Petrol";
                dataTransfer = new Object[2];
                url = getUrl(latitude, longitude, school);
                getNearbuyPlacesData = new GetNearbuyPlacesData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbuyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity1.this, "Showing Nearby ", Toast.LENGTH_LONG).show();
                break;

            case R.id.B_to:
                mMap.clear();
                dataTransfer = new Object[3];
                url = getDirectionsUrl();
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                getDirectionsData.execute(dataTransfer);
              //  mTextView = findViewById(R.id.Formula);
                //mTextView.setText(dataParser.getDistance());
                //Todo-------------------------------------------------------

                break;

            case R.id.Rider:
                mMap.clear();
                if (destinationLatLng!=null)
                userLocation.savePLocation(s,currentLatLng,destinationLatLng);
                UserDetails.username = s;
                mDatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sihsigma.firebaseio.com/Rlocation/");

                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("SNAPSHOT", Long.toString(dataSnapshot.getChildrenCount()));
                        if(destinationLatLng!=null){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            LatLng latLng1, latLng2;
                            snapshot.getKey();
                            latLng1 = new LatLng(snapshot.child("source").child("latitude").getValue(Double.class), snapshot.child("source").child("longitude").getValue(Double.class));
                            latLng2 = new LatLng(snapshot.child("destination").child("latitude").getValue(Double.class), snapshot.child("destination").child("longitude").getValue(Double.class));
                            String user = snapshot.getKey();
                            //MarkerOptions markerOptions1 = new MarkerOptions();

                            Log.e("users names", user);
                            mMap.addMarker(new MarkerOptions().icon(defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(latLng1)).setTitle(user);
                            mMap.addMarker(new MarkerOptions().icon(defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(latLng2)).setTitle(user);
                        }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;


            case  R.id.Passenger:
                mMap.clear();
                if(destinationLatLng != null)
                userLocation.saveRLocation(s,currentLatLng,destinationLatLng);

                mDatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sihsigma.firebaseio.com/Plocation/");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("SNAPSHOT", Long.toString(dataSnapshot.getChildrenCount()));

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            LatLng latLng1,latLng2;
                            snapshot.getKey();
                            if(destinationLatLng != null) {
                                latLng1 = new LatLng(snapshot.child("source").child("latitude").getValue(Double.class), snapshot.child("source").child("longitude").getValue(Double.class));
                                latLng2 = new LatLng(snapshot.child("destination").child("latitude").getValue(Double.class), snapshot.child("destination").child("longitude").getValue(Double.class));
                                mMap.addMarker(new MarkerOptions().icon(defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).position(latLng1)).setTitle(snapshot.getKey());
                                mMap.addMarker(new MarkerOptions().icon(defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).position(latLng2)).setTitle(snapshot.getKey());
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                    break;
        }
        }catch (Exception e){
            Toast.makeText(MapsActivity1.this,"Enter Distance ",Toast.LENGTH_LONG).show();
        }
    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyD2xSbh-5VlagBSsxTk3smQJGjnQ8OpeOE");

        return (googleDirectionsUrl.toString());
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyD2xSbh-5VlagBSsxTk3smQJGjnQ8OpeOE");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }




    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        markerOptions.draggable(true);
        markerOptions.title("Current Position");
        markerOptions.icon(defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        currentLatLng = latLng;
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        Toast.makeText(MapsActivity1.this,"Your Current Location", Toast.LENGTH_LONG).show();


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {


                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(false);
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
//        end_latitude = marker.getPosition().latitude;
//        end_longitude =  marker.getPosition().longitude;
//
//        Log.d("end_lat",""+end_latitude);
//        Log.d("end_lng",""+end_longitude);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            if (i == 1) {
                double distance = Double.parseDouble(DataParser.parsedDistance) / 1000;
                Log.e("distance mumbai", DataParser.parsedDistance);
                Log.e("distance", String.valueOf(distance));
                createAlert(String.valueOf(distance * 0.17));
            } else if (i == 2) {
                double distance = Double.parseDouble(DataParser.parsedDistance) / 1000;
                Log.e("distance mumbai", DataParser.parsedDistance);
                Log.e("distance", String.valueOf(distance));
                createAlert(String.valueOf(distance * 0.22));
            } else if (i == 3) {
                double distance = Double.parseDouble(DataParser.parsedDistance) / 1000;
                Log.e("distance mumbai", DataParser.parsedDistance);
                Log.e("distance", String.valueOf(distance));
                createAlert(String.valueOf(distance * 0.27));
            } else if (i == 4) {
                double distance = Double.parseDouble(DataParser.parsedDistance) / 1000;
                Log.e("distance mumbai", DataParser.parsedDistance);
                Log.e("distance", String.valueOf(distance));
                createAlert(String.valueOf(distance * 0.15));
            } else if (i == 5) {
                double distance = Double.parseDouble(DataParser.parsedDistance) / 1000;
                Log.e("distance mumbai", DataParser.parsedDistance);
                Log.e("distance", String.valueOf(distance));

                createAlert(String.valueOf(distance * 0.18));
            }

            else if(i==6){
                double distance = Double.parseDouble(DataParser.parsedDistance) / 1000;
                Log.e("distance mumbai", DataParser.parsedDistance);
                Log.e("distance", String.valueOf(distance));

                createAlert(String.valueOf(distance * 0.22));
            }

            else if(i == 7){
                double distance = Double.parseDouble(DataParser.parsedDistance) / 1000;
                Log.e("distance mumbai", DataParser.parsedDistance);
                Log.e("distance", String.valueOf(distance));

                createAlert(String.valueOf(distance * 0.095));
            }
        }catch (Exception e){
            createAlert("0");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void createAlert(String carbon){
        if(carbon == "0"){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your CO2 Emission is " + carbon + " Kgs Please select Destination and click directions").setCancelable(false)
                    .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Select Destination and Directions");
            alert.show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your CO2 Emission is " + carbon + " Kgs").setCancelable(false)
                    .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Your Co2 Emmision");
            alert.show();
        }
    }

}


