package com.example.hoangcongtuan.combannau.Utils;

import com.example.hoangcongtuan.combannau.models.User;

/**
 * Create by hoangcongtuan on 10/7/18.
 */
public class AppUserManager {
    private static AppUserManager sInstance;
    private User user;
    private String uid;

    private AppUserManager() {
        this.user = new User();
    }

    public static AppUserManager getInstance() {
        if (sInstance == null)
            sInstance = new AppUserManager();
        return sInstance;
    }

    public void setCurrentUser(User user, String uid) {
        this.user = user;
        this.uid = uid;
    }

    public User getCurrentUser() {
        return this.user;
    }

    public String getUid() {
        return uid;
    }
}
