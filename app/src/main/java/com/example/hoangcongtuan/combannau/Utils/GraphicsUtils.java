package com.example.hoangcongtuan.combannau.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by hoangcongtuan on 10/7/18.
 */
public class GraphicsUtils {
    private static GraphicsUtils sInstance;
    private Context mContext;

    private GraphicsUtils(Context context) {
        mContext = context.getApplicationContext();
    }

    public static synchronized GraphicsUtils getsInstance(Context context) {
        if (sInstance == null)
            sInstance = new GraphicsUtils(context);
        return sInstance;
    }

    public Bitmap getBitmapFromUri(Uri uri) throws FileNotFoundException {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap scaleBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap.getWidth() <= maxWidth || bitmap.getHeight() <= maxHeight)
            return bitmap;

        float scale = maxWidth * 1.0f / bitmap.getWidth();
        Bitmap res = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * scale),
                (int)(bitmap.getHeight() * scale), true);

        return res;

    }
}
