package com.example.hoangcongtuan.combannau.provider.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hoangcongtuan.combannau.MenuManager;
import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.Utils.AppUserManager;
import com.example.hoangcongtuan.combannau.Utils.Utils;
import com.example.hoangcongtuan.combannau.adapter.RVProviderPostAdapter;
import com.example.hoangcongtuan.combannau.interfaces.RecyclerTouchListener;
import com.example.hoangcongtuan.combannau.models.Menu;
import com.example.hoangcongtuan.combannau.models.Post;
import com.example.hoangcongtuan.combannau.models.PostObj;
import com.example.hoangcongtuan.combannau.provider.activity.CreateMenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by hoangcongtuan on 9/6/17.
 */

public class ProviderPostFragment extends Fragment {
    private static final String TAG = ProviderPostFragment.class.getName();
    private RecyclerView recyclerView;
    private RVProviderPostAdapter adapter;
    public static final String MID_DOT = " " + '\u00B7' + " ";
    //if hash not null, scroll to new feed has hashkey == hash
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup;
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_provider_post, container, false);
        return  viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupWidget();
        downloadMyPost();
    }

    private void setupWidget() {
        recyclerView = getView().findViewById(R.id.rvFeeds);
        adapter = new RVProviderPostAdapter(recyclerView, getContext());
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                viewMenu(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void viewMenu(int position) {
        Intent intent = new Intent(getActivity(), CreateMenuActivity.class);
        intent.putExtra(CreateMenuActivity.KEY_ACT_MODE, CreateMenuActivity.MODE_VIEW);
        intent.putExtra(CreateMenuActivity.KEY_MENU_POSITION, position);
        startActivity(intent);
    }

    private void downloadMyPost() {
//        DatabaseReference ref_menu = FirebaseDatabase.getInstance().getReference().child("user")
//                .child(AppUserManager.getInstance().getUid()).child("menu");
//        ref_menu.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists())
//                    return;
//                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    String menuKey = snapshot.getValue(String.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        DatabaseReference ref_menu = FirebaseDatabase.getInstance().getReference().child("menu");
        String ownerId = AppUserManager.getInstance().getUid();
        Query query = ref_menu.orderByChild("ownerId").equalTo(ownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Menu menu = snapshot.getValue(Menu.class);
                    adapter.addPost(menu);
                    MenuManager.getInstance().items.add(menu);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void update_new_post() {
        adapter.insertPost(0, MenuManager.getInstance().items.get(0));
    }

//    public void update_new_post() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference ref_post = FirebaseDatabase.getInstance().getReference()
//                .child("/user/" + currentUser.getUid());
//        Query query = ref_post.child("post").orderByKey().limitToLast(1);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        PostObj postObj = snapshot.getValue(PostObj.class);
//
//                        @SuppressLint("SimpleDateFormat")
//                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a");
//                        final Calendar calendar = Calendar.getInstance();
//                        try {
//                            calendar.setTime(
//                                    sdf.parse(postObj.getPostTime())
//                            );
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        final Post post = new Post(postObj, null, calendar.getTime(), null);
//
////                        final Post post = new Post(snapshot.getKey(), postObj.getMessage(),
////                                null, postObj.getImageUrl(), postObj.getRegion(), postObj.getPrice(),
////                                calendar, postObj.getTotal(), postObj.getEs(), postObj.latitude, postObj.longtitude, postObj.place);
//
//                        //calculate till_now
//                        long diffMs = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
//                        long diffSec = diffMs / 1000;
//                        long minute = diffSec / 60;
//                        long hour = diffSec / (60 * 60);
//                        String till_now = null;
//                        if (hour >= 24) {
//                            till_now = postObj.getPostTime();
//                        }
//                        else if (hour >= 1)
//                            till_now = hour + " giờ" + MID_DOT + post.getAddress();
//                        else if (minute >= 1)
//                            till_now = minute + " phút" + MID_DOT + post.getAddress();
//                        else
//                            till_now = diffSec + " giây" + MID_DOT + post.getAddress();
//
//
//                        post.setTillNow(till_now);
//
//                        if (!postObj.getImageUrl().isEmpty()) {
//                            ImageRequest avatarRequest = new ImageRequest(postObj.getImageUrl(),
//                                    new Response.Listener<Bitmap>() {
//                                        @Override
//                                        public void onResponse(Bitmap response) {
//                                            post.setImgBitmap(response);
//                                            adapter.notifyItemChanged(0);
//                                        }
//                                    },
//                                    0, 0,
//                                    ImageView.ScaleType.FIT_CENTER,
//                                    Bitmap.Config.RGB_565,
//                                    new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            //todo: error here
//                                            Log.d(TAG, "onErrorResponse: error");
//                                        }
//                                    }
//
//                            );
//                            Utils.VolleyUtils.getsInstance(getContext().getApplicationContext()).getRequestQueue().add(avatarRequest);
//                        }
//
//
//                        adapter.insertPost(0, post);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
