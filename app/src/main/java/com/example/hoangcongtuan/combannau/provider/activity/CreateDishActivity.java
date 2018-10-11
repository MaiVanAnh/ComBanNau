package com.example.hoangcongtuan.combannau.provider.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.Utils.AppUserManager;
import com.example.hoangcongtuan.combannau.Utils.Constants;
import com.example.hoangcongtuan.combannau.Utils.GraphicsUtils;
import com.example.hoangcongtuan.combannau.models.Dish;
import com.example.hoangcongtuan.combannau.models.DishObj;
import com.example.hoangcongtuan.combannau.models.PostObj;
import com.example.hoangcongtuan.combannau.widget.AddKeywordDialogBuilder;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateDishActivity extends AppCompatActivity {

    public static final int RC_PICK_IMAGE = 2;
    private static final int RC_PICK_PLACE = 3;
    public static final String KEY_DISH_OBJ = "KEY_DISH_OBJ";
    public static final String KEY_IMG_URI = "KEY_IMG_URI";
    private static final String TAG = CreateDishActivity.class.getName();
    @BindView (R.id.edtName)
    TextInputEditText edtName;
    @BindView(R.id.edtPrice)
    TextInputEditText edtPrice;
    @BindView(R.id.edtSetNumber)
    TextInputEditText edtSetNumber;
    @BindView(R.id.rbRegion)
    RadioGroup rgRegion;
    @BindView(R.id.tvKeyWords)
    TextView tvKeyWords;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imgCancel)
    ImageView imgCancel;
    @BindView(R.id.layout_create_post)
    CoordinatorLayout layout_create_post;
    @BindView(R.id.btnUpload)
    Button btnFinish;

    Uri uri_image;
    String strKeywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);
        ButterKnife.bind(this);
        init();
        initWidget();
    }

    private void init() {
        strKeywords = "";
    }

    private void initWidget() {
        findViewById(R.id.layout_keywords).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddKeyWordsDialog();
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

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtName.getText().toString();
                int price = Integer.parseInt(edtPrice.getText().toString());
                int total = Integer.parseInt(edtSetNumber.getText().toString());
                String region = getResources().getString(R.string.create_post_all);
                switch (rgRegion.getCheckedRadioButtonId()) {
                    case R.id.rbMienBac:
                        region = getResources().getString(R.string.create_post_mienBac);
                        break;

                    case R.id.rbMienTrung:
                        region = getResources().getString(R.string.create_post_mienTrung);
                        break;

                    case R.id.rbMienNam:
                        region = getResources().getString(R.string.create_post_mienNam);
                        break;

                    case R.id.rbAllRegion:
                        region = getResources().getString(R.string.create_post_all);
                        break;
                }

                final DishObj.Builder builder = new DishObj.Builder(title);
                builder.setPrice(price)
                        .setTotal(total)
                        .setRest(total)
                        .setRegion(region)
                        .setKeyWords(strKeywords);
                if (uri_image != null)
                    builder.setImageUrl(uri_image.toString());
                else
                    builder.setImageUrl("");
                finish(builder.build());
            }
        });

        imgCancel.setVisibility(View.INVISIBLE);

    }

    private void showAddKeyWordsDialog() {
        final AddKeywordDialogBuilder builder = new AddKeywordDialogBuilder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                strKeywords = builder.getKeyWords();
                tvKeyWords.setText(strKeywords);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void finish(DishObj dish) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_DISH_OBJ, dish);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void finish_post(boolean is_success) {
        Intent returnIntent = new Intent();
        if (is_success)
            setResult(RESULT_OK, returnIntent);
        else
            setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    private void chose_image() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(Intent.createChooser(galleryIntent,"Chọn hình ảnh từ thư viện"), RC_PICK_IMAGE);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_PICK_IMAGE);
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
                            Bitmap bitmap = GraphicsUtils.getsInstance(getApplicationContext()).getBitmapFromUri(uri_image);
                            bitmap = GraphicsUtils.getsInstance(getApplicationContext()).scaleBitmap(
                                    bitmap, Constants.MAX_WIDTH, Constants.MAX_HEIGHT
                            );
                            imageView.setImageBitmap(bitmap);
                            imgCancel.setVisibility(View.VISIBLE);
                        } catch (FileNotFoundException e) {
                            //TODO: error here
                            e.printStackTrace();
                        }
                        //uploadImage(uri_image);
                    }
                }
                break;
        }
    }

    private void uploadImage(final Uri uri) {
        final StorageReference ref_storage = FirebaseStorage.getInstance().getReference()
                .child(AppUserManager.getInstance().getUid()).child("/post/");

        ref_storage.child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            String child = uri.getLastPathSegment();
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref_storage.child(child).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: Uri = " + uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Get url failed");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Upload Failed");
            }
        });
    }
}
