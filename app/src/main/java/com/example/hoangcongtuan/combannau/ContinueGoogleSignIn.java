package com.example.hoangcongtuan.combannau;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContinueGoogleSignIn extends AppCompatActivity implements View.OnClickListener{
    private EditText edtUserName;
    private EditText edtAddress;
    private RadioGroup rgSex;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_google_sign_in);

        init();
        initWidget();
    }

    private void initWidget() {
        edtUserName = findViewById(R.id.edtUserName);
        edtAddress = findViewById(R.id.edtAddress);
        rgSex = findViewById(R.id.rgSex);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(this);
    }

    private void init() {

    }

    private void createAccount(String userName, String address, Boolean isMale) {
        //update user info to firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref_profile = reference.child("/user/" + currentUser.getUid() + "/profile/");
        ref_profile.child("user_name").setValue(userName);
        ref_profile.child("address").setValue(address);
        ref_profile.child("male").setValue(isMale);
        ref_profile.child("email").setValue(currentUser.getEmail());
        currentUser.sendEmailVerification();

        //go to verify screen
        Intent intent = new Intent(ContinueGoogleSignIn.this, RequestVerifyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                createAccount(edtUserName.getText().toString(), edtAddress.getText().toString(),
                        rgSex.getCheckedRadioButtonId() == R.id.rbMale);
                break;
        }
    }
}
