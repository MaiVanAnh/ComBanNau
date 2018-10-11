package com.example.hoangcongtuan.combannau.models;

import android.graphics.Bitmap;
import android.print.PrinterId;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hoangcongtuan on 3/23/18.
 */

public class Post extends PostObj {
    private Bitmap imgBitmap;
    private Date   datePost;
    private Date   dateEnd;
    private String tillNow;

    public Post(PostObj postObj, Bitmap imgBitmap, Date datePost, Date dateEnd) {
        super(postObj);
        this.imgBitmap = imgBitmap;
        this.datePost = datePost;
        this.dateEnd = dateEnd;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTillNow() {
        return tillNow;
    }

    public void setTillNow(String tillNow) {
        this.tillNow = tillNow;
    }
}
