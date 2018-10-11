package com.example.hoangcongtuan.combannau.Utils;

import android.content.Context;

/**
 * Create by hoangcongtuan on 10/11/18.
 */
public class LocationUtils {
    private static LocationUtils sInstance;
    private Context mContext;

    private LocationUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static LocationUtils getInstance(Context context) {
        if (sInstance == null)
            sInstance = new LocationUtils(context);
        return sInstance;
    }

}
