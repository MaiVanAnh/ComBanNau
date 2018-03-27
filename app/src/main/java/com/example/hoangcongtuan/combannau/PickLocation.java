package com.example.hoangcongtuan.combannau;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickLocation extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{

    private static final String TAG = PickLocation.class.getName();
    public static final String KEY_LAT = "LATTIUDE";
    public static final String KEY_LONG = "LONGTITUDE";
    public static final String KEY_PLACE = "PLACE";
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    private CoordinatorLayout layout_pick_location;

    private final static int RC_LOCATION = 1;
    private GoogleMap googleMap;

    private Marker pick_marker;
    private Place pick_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);

        init();
        initWidget();
        initMapFragment();
    }

    private void initWidget() {

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        layout_pick_location = findViewById(R.id.layout_pick_location);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                pick_place = place;
                if (pick_marker != null)
                    pick_marker.remove();
                pick_marker = googleMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                Snackbar.make(layout_pick_location, R.string.is_your_location, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                return_pick_location(place);
                            }
                        }).show();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void return_pick_location(Place place) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_LAT, (float) place.getLatLng().latitude);
        returnIntent.putExtra(KEY_LONG, (float) place.getLatLng().longitude);
        returnIntent.putExtra(KEY_PLACE, place.getAddress().toString());
        setResult(RESULT_OK, returnIntent);

        finish();
    }

    private void init() {
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // TODO: Start using the Places API.
    }

    private void initMapFragment() {
        ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);



        MapFragment mapFragment = ((MapFragment)getFragmentManager().findFragmentById(R.id.map));
        View locationButton = ((View)mapFragment.getView().findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("2"));

        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp.setMargins(30, 0, 0, 30);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(PickLocation.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(PickLocation.this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_LOCATION);
            }

            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setOnMyLocationButtonClickListener(this);
                googleMap.setOnMyLocationClickListener(this);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
