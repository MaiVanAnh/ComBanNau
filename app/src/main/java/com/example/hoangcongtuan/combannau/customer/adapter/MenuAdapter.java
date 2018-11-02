package com.example.hoangcongtuan.combannau.customer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.customer.RVMenuCallBack;
import com.example.hoangcongtuan.combannau.models.Dish;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by hoangcongtuan on 10/7/18.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ItemViewHolder> {
    List<Dish> items;
    RVMenuCallBack callBack;
    boolean showRemove;

    public MenuAdapter(List<Dish> items, boolean showRemove) {
        this.items = items;
        this.showRemove = showRemove;
    }

    public void setCallBack(RVMenuCallBack callBack) {
        this.callBack = callBack;
    }

    public void replace(List<Dish> rpItems) {
        this.items.clear();
        this.items.addAll(rpItems);
    }

    @NonNull
    @Override
    public MenuAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.layout_customer_item_overview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ItemViewHolder holder, final int position) {
        Dish item = items.get(position);
        holder.imgFood.setImageBitmap(item.bitmap);
        holder.tvTitle.setText(item.title);
        holder.tvRegion.setText(item.region);
        holder.tvPrice.setText(
                String.format(Locale.US, "%d.000 vnd", item.price)
        );
        holder.tvTotal.setText(item.total + "");
        holder.imgOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null)
                    callBack.onOrder(position);
            }
        });
        holder.imgRemove.setVisibility(showRemove == true?View.VISIBLE:View.INVISIBLE);
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
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvRegion)
        TextView tvRegion;
        @BindView(R.id.tvTotal)
        TextView tvTotal;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.imgOrder)
        ImageView imgOrder;
        @BindView(R.id.imgRemove)
        ImageView imgRemove;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
