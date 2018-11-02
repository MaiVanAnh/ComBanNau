package com.example.hoangcongtuan.combannau.Utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hoangcongtuan on 3/23/18.
 */

public class Common  {
    private static Common instance;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a", Locale.US);


    private FirebaseUser user;
    private Bitmap bmpAvatar;
    private String userName;


    public Common() {
        userName = null;
        bmpAvatar = null;
        user = null;
    }

    public static void init(FirebaseUser user, Bitmap bmpAvatar, String userName) {
        getInstance().bmpAvatar = bmpAvatar;
        getInstance().user = user;
        getInstance().userName = userName;
    }

    public static Common getInstance() {
        if (instance == null)
            instance = new Common();
        return instance;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public Bitmap getBmpAvatar() {
        return bmpAvatar;
    }

    public void setBmpAvatar(Bitmap bmpAvatar) {
        this.bmpAvatar = bmpAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
