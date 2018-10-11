package com.example.hoangcongtuan.combannau.customer.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hoangcongtuan.combannau.LoginActivity;
import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.Utils.Common;
import com.example.hoangcongtuan.combannau.Utils.Utils;
import com.example.hoangcongtuan.combannau.services.GPSTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{

    private final static int RC_LOCATION = 1;
    private static final String TAG = MainActivity.class.getName();

    private GoogleMap googleMap;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView tvUserName;
    private TextView tvEmail;
    private LatLng current_latlng;
//    private FirebaseFunctions mFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initWidget();

        initMapFragment();
    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        mFunctions = FirebaseFunctions.getInstance();
    }

    private void initWidget() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);

        tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        ActionBarDrawerToggle toggleButton = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggleButton);

        toggleButton.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //get avatar
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref_avatar_url = FirebaseDatabase.getInstance().getReference()
                .child("/user/" + currentUser.getUid() + "/profile/avatar_url");
        ref_avatar_url.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String avatar_url = "";
                if (dataSnapshot.exists())
                    avatar_url = dataSnapshot.getValue().toString();
                else if (currentUser.getPhotoUrl() != null)
                    avatar_url = currentUser.getPhotoUrl().toString();
                if (!avatar_url.isEmpty()) {
                    ImageRequest avatarRequest = new ImageRequest(avatar_url,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    ImageView imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
                                    imageView.setImageBitmap(response);
                                    Common.getInstance().setBmpAvatar(response);
                                }
                            },
                            0, 0,
                            ImageView.ScaleType.FIT_CENTER,
                            Bitmap.Config.RGB_565,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //todo: error here
                                    Log.d(TAG, "onErrorResponse: error");
                                }
                            }
                    );

                    Utils.VolleyUtils.getsInstance(getApplicationContext()).getRequestQueue().add(avatarRequest);
                }
                else {
                    ImageView imageView = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar_circle_blue_512dp));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref_user_name = FirebaseDatabase.getInstance().getReference()
                .child("/user/" + currentUser.getUid() + "/profile/user_name");
        ref_user_name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tvUserName.setText(dataSnapshot.getValue().toString());
                }
                else {
                    //todo: error here
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tvEmail.setText(currentUser.getEmail());

        Common.getInstance().setUserName(tvUserName.getText().toString());
        Common.getInstance().setUser(FirebaseAuth.getInstance().getCurrentUser());



    }

    private void initMapFragment() {
        ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    private void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        mGoogleSignInClient.signOut();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_cat_dat:
                break;
            case R.id.item_dang_xuat:
                logout();
                break;
            case R.id.item_functions:
                //add_message_functions();
                https_functions();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void https_functions() {
        GetPostsTask get_https_json = new GetPostsTask();
        get_https_json.execute("https://us-central1-combannau-1520822090740.cloudfunctions.net/getPosts");
    }

    private void show_provider(JSONObject json_posts) {
        for(Iterator key = json_posts.keys(); key.hasNext();) {
            try {
                JSONObject post = (JSONObject) json_posts.get((String) key.next());
                LatLng latLng = new LatLng(post.getDouble("latitude"), post.getDouble("longtitude"));
                Marker marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(post.getString("message"))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "show_provider: JSON_POSTS = " + json_posts.toString());
    }

    private class GetPostsTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject json_posts = new JSONObject(result);
                show_provider(json_posts);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_LOCATION);
            }

            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            current_latlng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_latlng, 15));
        }
        else {
            gpsTracker.showSettingsAlert();
        }
        https_functions();


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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
