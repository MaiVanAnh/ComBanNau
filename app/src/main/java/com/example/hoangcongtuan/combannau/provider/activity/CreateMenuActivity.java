package com.example.hoangcongtuan.combannau.provider.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.Utils.AppUserManager;
import com.example.hoangcongtuan.combannau.Utils.Constants;
import com.example.hoangcongtuan.combannau.Utils.GraphicsUtils;
import com.example.hoangcongtuan.combannau.models.Dish;
import com.example.hoangcongtuan.combannau.models.DishObj;
import com.example.hoangcongtuan.combannau.models.Menu;
import com.example.hoangcongtuan.combannau.provider.adapter.MenuAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateMenuActivity extends AppCompatActivity {

    private static final int RC_CREATE_DISH = 2;
    private static final int STATE_UPLOADING = 0;
    private static final int STATE_UPLOAD_SUCCESS = 1;
    private static final int STATE_UPLOAD_FAILED = 2;
    private static final String TAG = CreateMenuActivity.class.getName();
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.rvMenu)
    RecyclerView rvMenu;
    @BindView(R.id.btnAddDish)
    Button btnAdd;
    @BindView(R.id.tvTotalMenu)
    TextView tvTotalMenu;

    LatLng latLngAddress;
    String strAddress;
    String strEndTime;
    String strEndTimeUS;
    String ownerId;
    Menu menu;
    Menu.Builder menuBuilder;
    MenuAdapter adapter;
    int [] uploadState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);
        ButterKnife.bind(this);
        init();
        initWidget();
    }

    private void init() {
        strAddress = AppUserManager.getInstance().getCurrentUser().address;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a", Locale.getDefault());
        SimpleDateFormat sdfUS = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a", Locale.US);
        strEndTime = sdf.format(calendar.getTime());
        strEndTimeUS = sdfUS.format(calendar.getTime());

        menuBuilder = new Menu.Builder(AppUserManager.getInstance().getUid());
        adapter = new MenuAdapter(new ArrayList<Dish>());
    }

    private void initWidget() {
        tvAddress.setText(strAddress);
        tvEndTime.setText(strEndTime);

        rvMenu.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMenu.setItemAnimator(new DefaultItemAnimator());
        rvMenu.setAdapter(adapter);

        tvTotalMenu.setText(String.format(Locale.getDefault(), "Tổng: %d", adapter.getItemCount()));

        findViewById(R.id.layout_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateMenuActivity.this, PickLocation.class);
                startActivityForResult(intent, PickLocation.RC_PICK_LOCATION);
            }
        });

        findViewById(R.id.layout_endTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPickEndTimeDialog();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateMenuActivity.this, CreateDishActivity.class);
                startActivityForResult(intent, RC_CREATE_DISH);
            }
        });
    }

    private void showPickEndTimeDialog() {
        Calendar currentCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a", Locale.getDefault());
        try {
            Date date = sdf.parse(strEndTime);
            currentCalendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int currenHour = currentCalendar.get(Calendar.HOUR);
        int currentMinute = currentCalendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateMenuActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a", Locale.getDefault());
                SimpleDateFormat sdfUS = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a", Locale.US);
                strEndTime = sdf.format(calendar.getTime());
                strEndTimeUS = sdfUS.format(calendar.getTime());
                tvEndTime.setText(strEndTime);
            }
        }, currenHour, currentMinute, false);
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PickLocation.RC_PICK_LOCATION:
                if (resultCode == Activity.RESULT_OK)
                    getAddress(data);
                break;
            case RC_CREATE_DISH:
                if (resultCode == Activity.RESULT_OK) {
                    DishObj obj = (DishObj) data.getSerializableExtra(CreateDishActivity.KEY_DISH_OBJ);
                    menuBuilder.addItem(obj);
                    Dish dish = new Dish(obj);
                    Uri uri = Uri.parse(obj.imageUrl);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    if (uri != null) {
                        try {
                            bitmap = GraphicsUtils.getsInstance(getApplicationContext()).getBitmapFromUri(uri);
                            bitmap = GraphicsUtils.getsInstance(getApplicationContext()).scaleBitmap(bitmap, Constants.MAX_WIDTH, Constants.MAX_HEIGHT);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    dish.bitmap = bitmap;

                    adapter.getItems().add(dish);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    tvTotalMenu.setText(String.format(Locale.getDefault(), "Tổng: %d", adapter.getItemCount()));
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_finish:
                // TODO: 10/7/18 Finish action
                uploadImage();
                break;
        }
        return true;
    }

    private void uploadImage() {
        menu = menuBuilder.build();
        uploadState = new int[menu.items.size()];
        Arrays.fill(uploadState, STATE_UPLOADING);


        final StorageReference ref_storage = FirebaseStorage.getInstance().getReference()
                .child(AppUserManager.getInstance().getUid()).child("/post/");

        for(int i = 0; i < menu.items.size(); i++) {
            DishObj obj = menu.items.get(i);
            Uri uri = Uri.parse(obj.imageUrl);
            if (uri == null) {
                // TODO: 10/9/18 Handle Error
                uploadState[i] = STATE_UPLOAD_FAILED;
            } else {
                final int finalI = 0;
                final String fileName = "Image " + i;
                ref_storage.child(fileName).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    int tmp_i = finalI;
                    String child = fileName;
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref_storage.child(child).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                menu.items.get(tmp_i).imageUrl = uri.toString();
                                uploadState[tmp_i] = STATE_UPLOAD_SUCCESS;
                                checkUploadFinish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                uploadState[tmp_i] = STATE_UPLOAD_FAILED;
                                checkUploadFinish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    int tmp_i = finalI;
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadState[tmp_i] = STATE_UPLOAD_FAILED;
                        checkUploadFinish();
                    }
                });
            }
        }
    }

    private void checkUploadFinish() {
        for(int i = 0; i < uploadState.length; i++) {
            if (uploadState[i] == STATE_UPLOADING)
                return;
        }

        //all will upload is finished, but i can be failed
        for(int i = 0; i< uploadState.length; i++) {
            if (uploadState[i] == STATE_UPLOAD_FAILED) {
                showErrorWhenUpload();
                return;
            }
        }
        uploadMenu();
    }

    private void showErrorWhenUpload() {
        Toast.makeText(CreateMenuActivity.this, R.string.menu_upload_failed, Toast.LENGTH_SHORT).show();
    }

    private void uploadMenu() {
        final DatabaseReference ref_menu = FirebaseDatabase.getInstance().getReference().child("menu");
        final DatabaseReference ref_owner_menu = FirebaseDatabase.getInstance().getReference().child("user")
                .child(AppUserManager.getInstance().getUid()).child("menu");

        final String key = ref_menu.push().getKey();
        ref_menu.child(key).setValue(menu).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                ref_owner_menu.getT
                // TODO: 10/9/18 Keep order when push new value, top is newest
                ref_owner_menu.child(key).setValue(key);
                Toast.makeText(CreateMenuActivity.this, R.string.menu_uploaded, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateMenuActivity.this, R.string.menu_upload_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getAddress(Intent data) {
        if (data != null) {
            latLngAddress = new LatLng(data.getFloatExtra(PickLocation.KEY_LAT, 0)
                    , data.getFloatExtra(PickLocation.KEY_LONG, 0));
            strAddress = data.getStringExtra(PickLocation.KEY_PLACE);
            tvAddress.setText(strAddress);
        }
    }
}
