package com.example.hoangcongtuan.combannau.models;

import android.net.Uri;

import java.util.Calendar;

/**
 * Created by hoangcongtuan on 3/23/18.
 */

public class PostObj {
    public String message;
    public String imageUrl;
    public String region;
    public String price;
    public String time;
    public int total;
    public int es;
    public Float latitude;
    public Float longtitude;
    public String place;

    public PostObj() {

    }

    public PostObj(String message, String imageUrl, String region, String price,
                   String time, int total, int es, float latitude, float longtitude, String place) {
        this.message = message;
        this.imageUrl = imageUrl;
        this.region = region;
        this.time = time;
        this.price = price;
        this.total = total;
        this.es = es;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.place = place;

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
}
