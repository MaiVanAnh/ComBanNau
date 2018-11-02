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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static String getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return jsonString;
    }

    public static String getTimeString(String strDateTime) throws ParseException {
        Calendar dateTime = Calendar.getInstance();
        SimpleDateFormat sdfUS = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm a", Locale.US);

        dateTime.setTime(
                sdfUS.parse(strDateTime)
        );

        String timeString = "";
        SimpleDateFormat sdf;
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_YEAR) == dateTime.get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR)) {
            sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        }
        else if (calendar.get(Calendar.WEEK_OF_YEAR) == dateTime.get(Calendar.WEEK_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR)){
            sdf = new SimpleDateFormat("EEEE hh:mm a", Locale.getDefault());
        }
        else {
            sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        }

        timeString = sdf.format(dateTime.getTime());

        return timeString;
    }

    public static String getTillNow(String strTime) throws ParseException {
        Calendar startCal = Calendar.getInstance();

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(
                Common.sdf.parse(strTime)
        );

        long milis = endCal.getTimeInMillis() - startCal.getTimeInMillis();

        long second = milis / 1000;
        long minute = second / 60;

        long hour = minute / 60;

        String result = "";
        if (hour == 0)
            result = minute + " phút";
        else {
            result = hour + " giờ " + minute % 60 + " phút";
        }
        return "Còn " + result;
    }
}
