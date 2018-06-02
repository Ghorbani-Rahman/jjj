package com.delaroystodios.metakar.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.delaroystodios.metakar.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowAddressMyAdvertisementActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double mapX , mapY;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_address_my_advertisement);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(ShowAddressMyAdvertisementActivity.this);

        Bundle b = getIntent().getExtras();
        mapX = b.getDouble("map_x");
        mapY =b.getDouble("map_y");

    }

    public void onZoom(View view)
    {
        if(view.getId() == R.id.zoomin)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }

        if(view.getId() == R.id.zoomout)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(mapX , mapY);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,
                15));


        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));


    }

}
