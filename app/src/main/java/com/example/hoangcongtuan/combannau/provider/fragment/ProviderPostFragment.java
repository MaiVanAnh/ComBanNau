package com.example.hoangcongtuan.combannau.provider.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        adapter.setOnActionPerform(new RVProviderPostAdapter.OnActionPerform() {
            @Override
            public void onRemove(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.remove_menu);
                builder.setMessage(R.string.remove_menu_explain);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeMenu(position);
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }

            @Override
            public void onEdit(View view, int position) {
                editMenu(position);
            }
        });
    }

    private void removeMenu(final int position) {
        DatabaseReference ref_menu = FirebaseDatabase.getInstance().getReference().child("menu");
        final Menu menu = MenuManager.getInstance().items.get(position);
        ref_menu.child(menu.id).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseReference ref_owner_menu = FirebaseDatabase.getInstance().getReference()
                        .child("user").child(AppUserManager.getInstance().getUid()).child("menu");
                ref_owner_menu.child(menu.id).setValue(null);
                MenuManager.getInstance().items.remove(position);
                adapter.getPosts().remove(position);
                adapter.notifyItemRemoved(position);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), R.string.error_removed_menu, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editMenu(int position) {
        Intent intent = new Intent(getActivity(), CreateMenuActivity.class);
        intent.putExtra(CreateMenuActivity.KEY_ACT_MODE, CreateMenuActivity.MODE_EDIT);
        intent.putExtra(CreateMenuActivity.KEY_MENU_POSITION, position);
        startActivity(intent);
    }

    private void viewMenu(int position) {
        Intent intent = new Intent(getActivity(), CreateMenuActivity.class);
        intent.putExtra(CreateMenuActivity.KEY_ACT_MODE, CreateMenuActivity.MODE_VIEW);
        intent.putExtra(CreateMenuActivity.KEY_MENU_POSITION, position);
        startActivity(intent);
    }

    private void downloadMyPost() {
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
}
