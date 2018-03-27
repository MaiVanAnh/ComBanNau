package com.example.hoangcongtuan.combannau;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hoangcongtuan.combannau.Utils.Common;
import com.example.hoangcongtuan.combannau.Utils.Utils;
import com.example.hoangcongtuan.combannau.adapter.ProviderPagerAdapter;
import com.example.hoangcongtuan.combannau.fragment.BookFragment;
import com.example.hoangcongtuan.combannau.fragment.ProviderPostFragment;
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

public class ProviderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = ProviderActivity.class.getName().toString();
    private static final int RC_CREATE_POST = 1;
    private TextView tvUserName;
    private TextView tvEmail;
    private GoogleSignInClient mGoogleSignInClient;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProviderPagerAdapter adapter;
    private ProviderPostFragment postFragment;
    private BookFragment bookFragment;
    private FloatingActionButton fab_new_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        init();
        initWidget();

        doSomeThing();
    }

    private void doSomeThing() {
//        Intent intent = new Intent(ProviderActivity.this, PickLocation.class);
//        startActivity(intent);
    }

    private void initWidget() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);

        tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);

        fab_new_post = findViewById(R.id.fab_new_post);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        ActionBarDrawerToggle toggleButton = new ActionBarDrawerToggle(ProviderActivity.this,
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
                    Common.getInstance().setUserName(tvUserName.getText().toString());
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

        //init view pager
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new ProviderPagerAdapter(getSupportFragmentManager());
        //add fragment

        postFragment = new ProviderPostFragment();
        bookFragment = new BookFragment();

        adapter.addFragment(postFragment, getResources().getString(R.string.food_feed));
        adapter.addFragment(bookFragment, getResources().getString(R.string.book));

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

        fab_new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderActivity.this, CreatePostActivity.class);
                startActivityForResult(intent, RC_CREATE_POST);
            }
        });
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

        Intent intent = new Intent(ProviderActivity.this, LoginActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_CREATE_POST:
                if (resultCode == RESULT_OK) {
                    postFragment.update_new_post();
                }
                break;
        }
    }
}
