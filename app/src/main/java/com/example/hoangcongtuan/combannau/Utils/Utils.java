package com.example.hoangcongtuan.combannau.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hoangcongtuan on 3/21/18.
 */

public class Utils {

    public static class VolleyUtils {
        private static final String TAG = Volley.class.getName();
        private static VolleyUtils sInstance;
        private RequestQueue requestQueue;

        private VolleyUtils(Context context) {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(context);
            }
        }

        public static synchronized VolleyUtils getsInstance(Context context) {
            if (sInstance == null)
                sInstance = new VolleyUtils(context);
            return  sInstance;
        }

        public RequestQueue getRequestQueue() {
            return requestQueue;
        }
    }

    public static class InternetUitls {
        private final static String TAG = InternetUitls.class.getName();
        private static InternetUitls sInstance;
        private Context mContext;

        private InternetUitls(Context context) {
            this.mContext = context;
        }

        public static InternetUitls getsInstance(Context context) {
            if (sInstance == null)
                sInstance = new InternetUitls(context);
            return sInstance;
        }

        public boolean isNetworkConnected() {
            ConnectivityManager cm =
                    (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }


        public boolean isInternetAvailable() {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
                return !ipAddr.equals("");

            } catch (Exception e) {
                return false;
            }

        }
    }

    public static Bitmap getThumbnails(String path, int width, int height) throws FileNotFoundException {
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), width, height);
        if (bitmap == null)
            throw new FileNotFoundException(path + " not found!");
        return bitmap;
    }
}
