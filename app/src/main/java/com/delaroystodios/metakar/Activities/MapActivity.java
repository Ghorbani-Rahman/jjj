package com.delaroystodios.metakar.Activities;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.PlaceAutoCompleteAdapter;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.fragment.StepTwoAddAdvertisement;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
      new LatLng(-40 , -168) , new LatLng(71 , 136));

    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    private String searchString;


    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private TextView acceptAddress;


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        String language= "fa_IR";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN){
            config.setLocale(locale);
            createConfigurationContext(config);
        }else {
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MapActivity.this.setMarker(latLng.latitude, latLng.longitude);
            }

        });

                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText = findViewById(R.id.input_search);
        mGps = findViewById(R.id.ic_gps);
        acceptAddress = findViewById(R.id.accept_select_map);

        getLocationPermission();
    }

    private void init()
    {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this , mGoogleApiClient , LAT_LNG_BOUNDS , null);

        mSearchText.setAdapter(mPlaceAutoCompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    geoLocate();
                }
                return false;
            }
        });

        acceptAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDeviceLocation();
            }
        });

    }


    private void setMarker( double lat, double lng) {


        mMap.clear();

        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(lat , lng));

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);

        StepTwoAddAdvertisement.addressAdvertisementMap = address;
        Log.i("addressAdvertisement" , address);
        StepTwoAddAdvertisement.Lat = lat;
        StepTwoAddAdvertisement.Lng = lng;


        mMap.addMarker(options);

        moveCamera(new LatLng(lat , lng) , DEFAULT_ZOOM , address);

    }

    private void geoLocate()
    {
        searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try
        {
            list = geocoder.getFromLocationName(searchString , 1);
        }catch (IOException e)
        {
            Log.d(TAG , "geoLocate: IOException : " + e.getMessage());
        }

        if(list.size() > 0)
        {
            Address address = list.get(0);

            MapActivity.this.setMarker(address.getLatitude(), address.getLongitude());


        }

    }

    private void getDeviceLocation()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try
        {
                    Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Location currentLocation = (Location) task.getResult();

                              /*  LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                MarkerOptions options = new MarkerOptions()
                                        .position(latLng)
                                        .zIndex(DEFAULT_ZOOM);
                                mMap.addMarker(options);*/

                              setMarker(currentLocation.getLatitude() , currentLocation.getLongitude());

                        //        moveCamera(latLng, DEFAULT_ZOOM, "شما اینجا هستید!");


                                //StepTwoAddAdvertisement.addressAdvertisement =
                            } else {

                                Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        catch (SecurityException e)
        {
            Log.d(TAG , "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng , float zoom , String title)
    {
        mMap.clear();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng , zoom));

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            mMap.addMarker(options);

        hideSoftKeyboard();
    }

    private void initMap()
    {

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission()
    {

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION ,
                                Manifest.permission.ACCESS_COARSE_LOCATION };

        if(ContextCompat.checkSelfPermission(this.getApplicationContext() ,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext() ,
                    ACCESS_COARSE) == PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionGranted = true;
                initMap();
            }else
            {
                ActivityCompat.requestPermissions(this , permissions , LOCATION_PERMISSION_REQUEST_CODE );
            }
        }else
        {
            ActivityCompat.requestPermissions(this , permissions , LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        mLocationPermissionGranted = false;

        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length > 0)
                {
                    for(int i = 0; i < grantResults.length ; i++)
                    {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }

                    mLocationPermissionGranted = true;

                    initMap();

                }
            }
        }

    }

    private void hideSoftKeyboard()
    {
        Toast.makeText(this, "sear", Toast.LENGTH_SHORT).show();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutoCompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient , placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places)
        {
            if(!places.getStatus().isSuccess())
            {
                return;
            }

            final Place place = places.get(0);
            setMarker(place.getViewport().getCenter().latitude , place.getViewport().getCenter().longitude);

            places.release();
        }
    };

}