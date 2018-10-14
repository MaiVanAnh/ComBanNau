package com.example.hoangcongtuan.combannau;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hoangcongtuan.combannau.Utils.AppUserManager;
import com.example.hoangcongtuan.combannau.common.ProfileSetupActivity;
import com.example.hoangcongtuan.combannau.customer.activity.MainActivity;
import com.example.hoangcongtuan.combannau.models.User;
import com.example.hoangcongtuan.combannau.models.UserObj;
import com.example.hoangcongtuan.combannau.provider.activity.ProviderActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String CUSTOMER = "customer";
    public final static String PROVIDER = "provider";
    public final static String ADMIN    = "admin";

    private static final String TAG = LoginActivity.class.getName();
    private static final int RC_SIGN_IN = 1;
    private EditText    edtUserName;
    private EditText    edtPassword;
    private Button      btnLogin;
    private Button      btnLoginGG;
    private TextView    tvSignUp;
    private CoordinatorLayout layout_login;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initWidget();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initWidget() {

        layout_login = findViewById(R.id.layout_login);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGG = findViewById(R.id.btnLoginGg);

        tvSignUp = findViewById(R.id.tvSignUp);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(this);
        btnLoginGG.setOnClickListener(this);


    }

    private void init() {

        // Build a GoogleSignInClient with the options specified by gso.


    }

    public void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //check profile

                            //updateUI(user);
                            checkProfile(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(layout_login, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void checkProfile(final FirebaseUser user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref_user = reference.child("/user/" + user.getUid() + "/profile/");

        ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //have profile
                    //go to main activity
                    //check verified

                    if (!user.isEmailVerified()) {
                        Snackbar.make(layout_login, R.string.email_not_verified, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.resend_verified, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        user.sendEmailVerification();
                                    }
                                }).show();
                    }
                    else {
                        UserObj userObj = dataSnapshot.getValue(UserObj.class);
                        User user = new User(userObj);
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        AppUserManager.getInstance().setCurrentUser(user, uid);
                        check_user_type();
                    }
                }
                else {
                    //no profile
                    Intent intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (user_type.equals(LoginActivity.PROVIDER)) {
                        //is provider
                        //TODO: Go to provider activity
                        Intent intent = new Intent(LoginActivity.this, ProviderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //is customer
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    // ...
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String userName = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();
                if (userName.isEmpty() || password.isEmpty()) {
                    Snackbar.make(layout_login, R.string.please_type_full_info, Snackbar.LENGTH_INDEFINITE).show();
                    break;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(userName, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    //check profile
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference ref_user = reference.child("/user/" + currentUser.getUid() + "/profile/");

                                    ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                //have profile
                                                //check email verify
                                                if (!currentUser.isEmailVerified()) {
                                                    //email not verify
                                                    Snackbar.make(layout_login, R.string.email_not_verified, Snackbar.LENGTH_INDEFINITE)
                                                            .setAction(R.string.resend_verified, new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    currentUser.sendEmailVerification();
                                                                }
                                                            }).show();
                                                }
                                                else {
                                                    //email is verify, go to main Activity
                                                    Log.d(TAG, "onComplete: UID = " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    //check user type
                                                    UserObj userObj = dataSnapshot.getValue(UserObj.class);
                                                    User user = new User(userObj);
                                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    AppUserManager.getInstance().setCurrentUser(user, uid);
                                                    check_user_type();
                                                }
                                            }
                                            else {
                                                //no profile was set, goto profile setup
                                                Intent intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else {
                                    Snackbar.make(layout_login, R.string.login_failed, Snackbar.LENGTH_INDEFINITE)
                                            .setAction(R.string.details, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                    builder.setTitle(R.string.app_name);
                                                    builder.setMessage(task.getException().toString());
                                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                                    builder.create().show();
                                                }
                                            }).show();
                                }
                            }
                        });
                break;
            case R.id.btnLoginGg:
                googleSignIn();
                break;
        }

    }
}
