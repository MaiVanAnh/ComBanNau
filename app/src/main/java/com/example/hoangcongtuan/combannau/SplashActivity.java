package com.example.hoangcongtuan.combannau;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Space;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        initWidget();
    }

    private void initWidget() {

    }

    private void check_user_type() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref_user = FirebaseDatabase.getInstance().getReference()
                .child("/user/" + currentUser.getUid() + "/type");
        ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //TODO: have error here
                }
                else {
                    String user_type = (String) dataSnapshot.getValue();
                    if (user_type.equals(LoginActivity.ADMIN)) {
                        //is admin
                        Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (user_type.equals(LoginActivity.PROVIDER)) {
                        //is provider
                        //TODO: Go to provider activity
                        Intent intent = new Intent(SplashActivity.this, ProviderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //is customer
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            //already login
            //check profile
            DatabaseReference ref_profile = FirebaseDatabase.getInstance().getReference()
                    .child("/user/" + currentUser.getUid() + "/profile/");
            ref_profile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //da co profle
                        //check verify
                        if (!currentUser.isEmailVerified()) {
                            //not verify, goto login activity
                            FirebaseAuth.getInstance().signOut();

                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            check_user_type();
                        }
                    }
                    else {
                        //no profile was set, goto profie setup
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setMessage(R.string.need_profile_setup);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SplashActivity.this, ProfileSetupActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        builder.setCancelable(false);
                        builder.create().show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //TODO: error handle here
                }
            });
        }

        else {
            //not login yet
            //goto login actiivty
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
