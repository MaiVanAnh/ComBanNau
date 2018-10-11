package com.example.hoangcongtuan.combannau.provider.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.models.Dish;
import com.example.hoangcongtuan.combannau.models.DishObj;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by hoangcongtuan on 10/7/18.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ItemViewHolder> {
    List<Dish> items;

    public MenuAdapter(List<Dish> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MenuAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.layout_dish, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ItemViewHolder holder, int position) {
        Dish item = items.get(position);
        holder.imgFood.setImageBitmap(item.bitmap);
        holder.tvTitle.setText(item.title);
        holder.tvRegion.setText(item.region);
        holder.tvPrice.setText(
                String.format(Locale.US, "%d.000 vnd", item.price)
        );
        holder.tvTotal.setText(item.total + "");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public List<Dish> getItems() {
        return items;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgFood)
        ImageView imgFood;
        @BindView(R.id.imgRemove)
        ImageView imgRemove;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvRegion)
        TextView tvRegion;
        @BindView(R.id.tvTotal)
        TextView tvTotal;
        @BindView(R.id.tvPrice)
        TextView tvPrice;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
