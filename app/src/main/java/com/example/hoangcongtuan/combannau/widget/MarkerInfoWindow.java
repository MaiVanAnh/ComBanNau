package com.example.hoangcongtuan.combannau.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hoangcongtuan.combannau.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Create by hoangcongtuan on 10/14/18.
 */
public class MarkerInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    public MarkerInfoWindow(Context context) {
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_maker_info, null);
        ((TextView)view.findViewById(R.id.tvTitle)).setText(marker.getTitle());
        ((TextView)view.findViewById(R.id.tvContent)).setText(marker.getSnippet());
        return view;
    }
}
