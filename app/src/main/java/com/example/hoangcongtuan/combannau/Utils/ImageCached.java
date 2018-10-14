package com.example.hoangcongtuan.combannau.Utils;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Create by hoangcongtuan on 10/14/18.
 */
public class ImageCached {
    private static ImageCached sInstance;
    public HashMap<String, Bitmap> bitmapHashMap;

    private ImageCached() {
        bitmapHashMap = new HashMap<>();
    }

    public static ImageCached getInstance() {
        if (sInstance == null)
            sInstance = new ImageCached();
        return sInstance;
    }
}
