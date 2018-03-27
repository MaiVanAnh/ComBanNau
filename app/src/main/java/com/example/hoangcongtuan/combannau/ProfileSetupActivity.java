package com.example.hoangcongtuan.combannau;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hoangcongtuan.combannau.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileSetupActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = ProfileSetupActivity.class.getName();
    private static final int PICK_IMAGE = 1;
    private EditText edtUserName;
    private EditText edtAddress;
    private EditText edtPhone;
    private RadioGroup rgSex;
    private Button btnCreate;
    private ImageView imgAvatar;
    private CoordinatorLayout layout_profile_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        init();
        initWidget();
    }

    private void initWidget() {
        edtUserName = findViewById(R.id.edtUserName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        rgSex = findViewById(R.id.rgSex);
        btnCreate = findViewById(R.id.btnCreate);

        imgAvatar = findViewById(R.id.imgAvatar);

        layout_profile_setup = findViewById(R.id.layout_profile_setup);

        btnCreate.setOnClickListener(this);
        //download avatar

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser.getPhotoUrl() != null) {
            ImageRequest avatarRequest = new ImageRequest(currentUser.getPhotoUrl().toString(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imgAvatar.setImageBitmap(response);
                        }
                    },
                    0, 0,
                    ImageView.ScaleType.FIT_CENTER,
                    Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: error");
                        }
                    }
            );

            Utils.VolleyUtils.getsInstance(getApplicationContext()).getRequestQueue().add(avatarRequest);
        }
        else
            imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_circle_blue_512dp));


        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chose_image();
            }
        });

    }

    private void chose_image() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent,"Chọn hình ảnh từ thư viện"),PICK_IMAGE);
    }

    private void upload_avatar(final Uri uri) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child(currentUser.getUid() + "/avatar/" + uri.getLastPathSegment());
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //save image url to profile
                DatabaseReference ref_profile = FirebaseDatabase.getInstance().getReference()
                        .child("user/" + currentUser.getUid() + "/profile");
                ref_profile.child("avatar_url").setValue(taskSnapshot.getDownloadUrl().toString());

                //show image avatar
                try {
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    imgAvatar.setImageBitmap(bitmap);
                } catch (final FileNotFoundException e) {
                    //todo: error here
                    Snackbar.make(layout_profile_setup, R.string.error_upload_avatar, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.details, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetupActivity.this);
                                    builder.setTitle(R.string.app_name);
                                    builder.setMessage(e.getMessage());
                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.create().show();
                                }
                            });
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                //todo: erro here
                Snackbar.make(layout_profile_setup, R.string.error_upload_avatar, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.details, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetupActivity.this);
                                builder.setTitle(R.string.app_name);
                                builder.setMessage(e.getMessage());
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();
                            }
                        });
            }
        });

    }


    private void init() {

    }

    private void uploadProfile(String userName, String address, Boolean isMale) {
        //update user info to firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref_profile = reference.child("/user/" + currentUser.getUid() + "/profile/");
        ref_profile.child("user_name").setValue(userName);
        ref_profile.child("address").setValue(address);
        ref_profile.child("male").setValue(isMale);
        ref_profile.child("email").setValue(currentUser.getEmail());


        DatabaseReference ref_user = reference.child("/user/" + currentUser.getUid());
        ref_user.child("type").setValue(LoginActivity.CUSTOMER);
        //currentUser.sendEmailVerification();

        //goto main actiivy
        //check user type

        check_user_type();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE:
                if (data != null) {
                    crop_image(data.getData());
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri uri = result.getUri();
                upload_avatar(uri);
                break;

        }
    }

    private void crop_image(Uri uri) {
        CropImage.activity(uri).start(ProfileSetupActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                uploadProfile(edtUserName.getText().toString(), edtAddress.getText().toString(),
                        rgSex.getCheckedRadioButtonId() == R.id.rbMale);
                break;
        }
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
                        Intent intent = new Intent(ProfileSetupActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (user_type.equals(LoginActivity.PROVIDER)) {
                        //is provider
                        //TODO: Go to provider activity
                        Intent intent = new Intent(ProfileSetupActivity.this, ProviderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //is customer
                        Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
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
}
