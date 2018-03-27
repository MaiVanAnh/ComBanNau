package com.example.hoangcongtuan.combannau;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import com.example.hoangcongtuan.combannau.Utils.Common;
import com.example.hoangcongtuan.combannau.Utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = AdminActivity.class.getName().toString();
    private TextView tvUserName;
    private TextView tvEmail;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        init();
        initWidget();
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

        ActionBarDrawerToggle toggleButton = new ActionBarDrawerToggle(AdminActivity.this,
                drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggleButton);

        toggleButton.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref_profile = FirebaseDatabase.getInstance().getReference()
                .child("/user/" + currentUser.getUid() + "/profile/avatar_url");
        ref_profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String avatar_url = "";
                if (dataSnapshot.exists())
                    avatar_url = dataSnapshot.getValue().toString();
                else if (currentUser.getPhotoUrl() != null)
                    avatar_url = currentUser.getPhotoUrl().toString();
                if(!avatar_url.isEmpty()) {
                    ImageRequest avatarRequest = new ImageRequest(avatar_url,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    ImageView imageView = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
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

        tvEmail.setText(currentUser.getEmail());
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

        Common.getInstance().setUser(FirebaseAuth.getInstance().getCurrentUser());
        Common.getInstance().setUserName(tvUserName.getText().toString());
    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        mGoogleSignInClient.signOut();

        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_dang_xuat:
                logout();
                break;
        }
        return false;
    }
}
