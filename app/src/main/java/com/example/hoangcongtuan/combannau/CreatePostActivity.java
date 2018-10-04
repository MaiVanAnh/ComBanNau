package com.example.hoangcongtuan.combannau;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoangcongtuan.combannau.models.PostObj;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreatePostActivity extends AppCompatActivity {

    private static final int RC_PICK_IMAGE = 1;
    private static final int RC_PICK_PLACE = 2;
    private EditText edtContent;
    private TextView tvPickPlace;

    private ImageView imageView;
    private ImageView imgCancel;

    private CoordinatorLayout layout_create_post;

    private Button btnUpload;

    private Uri uri_image;
    private LatLng pick_latlng;
    private String pick_place;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed);
        init();
        initWidget();
    }

    private void initWidget() {
        edtContent = findViewById(R.id.edtContent);
        imageView = findViewById(R.id.imageView);
        imgCancel = findViewById(R.id.imgCancel);
        tvPickPlace = findViewById(R.id.tvPickPlace);

        layout_create_post = findViewById(R.id.layout_create_post);

        btnUpload = findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_post(edtContent.getText().toString(), uri_image, pick_latlng, pick_place);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chose_image();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.image_empty_border));
                imgCancel.setVisibility(View.INVISIBLE);
                uri_image = null;
            }
        });

        tvPickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePostActivity.this, PickLocation.class);
                startActivityForResult(intent, RC_PICK_PLACE);
            }
        });

        imgCancel.setVisibility(View.INVISIBLE);


    }

    private void upload_post(final String content, Uri uri_image, final LatLng latLng, final String place) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a");
        final String strTime = sdf.format(calendar.getTime());
        //upload image to storage
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //verify data
        if (content.isEmpty())
            Snackbar.make(layout_create_post, R.string.you_must_enter_content, Snackbar.LENGTH_LONG).show();
        else if (latLng.latitude == 0 || latLng.longitude == 0)
            Snackbar.make(layout_create_post, R.string.you_must_enter_place, Snackbar.LENGTH_LONG).show();
        else {
            final DatabaseReference ref_database = FirebaseDatabase.getInstance().getReference()
                    .child("/user/" + currentUser.getUid() + "/post/");

            if (uri_image != null) {
                final StorageReference ref_storage = FirebaseStorage.getInstance().getReference()
                        .child(currentUser.getUid() + "/post/" + uri_image.getLastPathSegment());
                ref_storage.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        ref_storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                PostObj postObj = new PostObj(content, uri.toString().toString(),
                                        "vietnam", "0vnd", strTime, 5, 5,
                                        (float)latLng.latitude, (float)latLng.longitude, place);

                                String key = ref_database.push().getKey();
                                ref_database.child(key).setValue(postObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            finish_post(true);
                                        else
                                            finish_post(false);
                                        //TODO: upload failed
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreatePostActivity.this, R.string.getUrl_failer, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO: upload failed
                        finish_post(false);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //TODO: on upload image progress
                    }
                });
            }
            else {
                PostObj postObj = new PostObj(content, "",
                        "vietnam", "0vnd", strTime, 5, 5,  (float)latLng.latitude, (float)latLng.longitude, place);
                String key = ref_database.push().getKey();
                ref_database.child(key).setValue(postObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            finish_post(true);
                        else
                            finish_post(false);
                        //TODO: upload failed
                    }
                });
            }
        }


    }

    private void finish_post(boolean is_success) {
        Intent returnIntent = new Intent();
        if (is_success)
            setResult(RESULT_OK, returnIntent);
        else
            setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    private void init() {
        pick_latlng = new LatLng(0, 0);
        pick_place = "";
    }

    private void chose_image() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent,"Chọn hình ảnh từ thư viện"), RC_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri_image = data.getData();
                        //show image
                        try {
                            InputStream imageStream = null;
                            imageStream = getContentResolver().openInputStream(uri_image);
                            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                            imageView.setImageBitmap(bitmap);
                            imgCancel.setVisibility(View.VISIBLE);
                        } catch (FileNotFoundException e) {
                            //TODO: error here
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case RC_PICK_PLACE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        pick_latlng = new LatLng(data.getFloatExtra(PickLocation.KEY_LAT, 0)
                            , data.getFloatExtra(PickLocation.KEY_LONG, 0));
                        pick_place = data.getStringExtra(PickLocation.KEY_PLACE);

                        tvPickPlace.setText(pick_place);

                    }
                }
                break;
        }
    }
}
