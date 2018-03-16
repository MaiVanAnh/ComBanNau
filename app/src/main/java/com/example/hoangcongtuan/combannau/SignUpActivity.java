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
import android.widget.RadioGroup;

import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SignUpActivity.class.getName();

    private EditText edtEmail;
    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtAddress;
    private RadioGroup rgSex;
    private Button btnCreate;
    private CoordinatorLayout layout_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        initWidget();
    }

    private void initWidget() {
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtAddress = findViewById(R.id.edtAddress);

        rgSex = findViewById(R.id.rgSex);

        btnCreate = findViewById(R.id.btnCreate);

        layout_signup = findViewById(R.id.layout_signup);

        btnCreate.setOnClickListener(this);

    }

    private void init() {

    }

    public void createAccount(final String userName, final String email, String password, final String address, final boolean isMale) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Success");

                            //upload user info to firebase

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref_user = reference.child("/user/" + currentUser.getUid() + "/profile/");
                            ref_user.child("/user_name").setValue(userName);
                            ref_user.child("/email").setValue(email);
                            ref_user.child("/address").setValue(address);
                            ref_user.child("/male").setValue(isMale);

                            currentUser.sendEmailVerification();

                            if (!currentUser.isEmailVerified()) {
                                //TODO: go to verify screen
                                Intent intent = new Intent(SignUpActivity.this, RequestVerifyActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Snackbar.make(layout_signup, R.string.create_account_failed, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.details, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

                                            builder.setTitle(R.string.app_name);
                                            builder.setMessage(task.getException().getMessage());
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                String userName = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();
                String email = edtEmail.getText().toString();
                String address = edtAddress.getText().toString();
                Boolean isMale = rgSex.getCheckedRadioButtonId() == R.id.rbMale;

                createAccount(userName, email, password, address, isMale);
                break;
        }
    }
}
