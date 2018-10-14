package com.example.hoangcongtuan.combannau.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.Utils.Common;
import com.example.hoangcongtuan.combannau.Utils.Utils;
import com.example.hoangcongtuan.combannau.models.Menu;
import com.example.hoangcongtuan.combannau.models.Post;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoangcongtuan on 3/23/18.
 */

public class RVProviderPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = RVProviderPostAdapter.class.getName();
    private ArrayList<Menu> menus;
    private Context mContext;

    private LinearLayoutManager linearLayoutManager;

    //type of item
    private final static int ITEM_LOADED = 0;
    private final static int ITEM_LOADING = 1;


    public RVProviderPostAdapter(final RecyclerView recyclerView, Context context) {
        menus = new ArrayList<>();
        linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        this.mContext = context;

    }

    //hold view cho moi item da load trong recycle view
    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvStartTime)
        TextView tvStartTime;
        @BindView(R.id.tvEndTime)
        TextView tvEndTime;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.imgRemove)
        ImageView imgRemove;

        public PostHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initWidget();
            itemView.setOnClickListener(this);
        }

        private void initWidget() {
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), R.string.remove_menu, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    //holder view cho item dang load trong recycle view
    public class PostHolderLoading extends RecyclerView.ViewHolder {
        public PostHolderLoading(View itemView) {
            super(itemView);
        }
    }

    //luc tao item cho recycleview, phu thuoc vao viewType
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case ITEM_LOADING:
                //item dang load
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_loading, parent, false);
                return new PostHolderLoading(itemView);
            case ITEM_LOADED:
                //item da load
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_overview, parent, false);
                return new PostHolder(itemView);
        }
        return null;
    }

    //hien thi du lieu len item
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PostHolder) {
            PostHolder postHolder = (PostHolder) holder;
            Menu menu = menus.get(position);

            postHolder.tvTitle.setText(menu.name);
            postHolder.tvAddress.setText(menu.address);

            try {
                postHolder.tvStartTime.setText(
                        Utils.getTimeString(menu.startTime)
                );
            } catch (ParseException e) {
                e.printStackTrace();
                postHolder.tvStartTime.setText(menu.startTime);
            }

            try {
                postHolder.tvEndTime.setText(
                        Utils.getTimeString(menu.endTime)
                );
            } catch (ParseException e) {
                e.printStackTrace();
                postHolder.tvEndTime.setText(menu.endTime);
            }

        }
    }

    public Context getContext() {
        return mContext;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
    //interface dung de call back moi khi load them thong bao
    public interface ILoadMoreCallBack {
        void onLoadMore();
        void onLoadMoreFinish();
    }

    //tra ve trang thai cua thong bao, tu do xac dinh dung viewholder nao
    @Override
    public int getItemViewType(int position) {
        return menus.get(position) == null ? ITEM_LOADING : ITEM_LOADED;
    }

    public ArrayList<Menu> getPosts() {
        return menus;
    }

    public void addPost(Menu menu) {
        menus.add(menu);
    }

    public void insertPost(int position, Menu menu) {
        menus.add(position, menu);
        this.notifyItemInserted(position);
    }
}
