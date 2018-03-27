package com.example.hoangcongtuan.combannau.models;

import android.graphics.Bitmap;

import java.util.Calendar;

/**
 * Created by hoangcongtuan on 3/23/18.
 */

public class Post {

    private String id;
    private String message;
    private Bitmap bitmap;
    private String till_now;

    private String place;
    private float longtitude;
    private float latitude;
    private String image_url;
    private String region;
    private String price;
    private Calendar time;
    private int total;
    private int es;

    public Post(String id, String message, Bitmap bitmap, String image_url,
                String region, String price, Calendar time, int total, int es, float latitude, float longtitude, String place) {
        this.id = id;
        this.message = message;
        this.bitmap = bitmap;
        this.image_url = image_url;
        this.region = region;
        this.price = price;
        this.time = time;
        this.total = total;
        this.es = es;

        this.latitude = latitude;
        this.longtitude = longtitude;
        this.place = place;
    }

    public String getTill_now() {
        return till_now;
    }

    public void setTill_now(String till_now) {
        this.till_now = till_now;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getEs() {
        return es;
    }

    public void setEs(int es) {
        this.es = es;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
