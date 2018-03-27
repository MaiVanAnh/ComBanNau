package com.example.hoangcongtuan.combannau.models;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by hoangcongtuan on 3/21/18.
 */

public class User {
    private String userName;
    private String email;
    private String type;
    private String region;
    private Boolean isMale;
    private String address;
    private String phone;
    private Bitmap avatar;

    public User(String userName, String email, String type, String region, Boolean isMale, String address, String phone, Bitmap avatar) {
        this.userName = userName;
        this.email = email;
        this.type = type;
        this.region = region;
        this.isMale = isMale;
        this.address = address;
        this.avatar = avatar;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
