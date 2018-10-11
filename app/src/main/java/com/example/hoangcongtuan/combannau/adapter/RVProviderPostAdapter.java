package com.example.hoangcongtuan.combannau.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoangcongtuan.combannau.R;
import com.example.hoangcongtuan.combannau.Utils.Common;
import com.example.hoangcongtuan.combannau.models.Post;

import java.util.ArrayList;

/**
 * Created by hoangcongtuan on 3/23/18.
 */

public class RVProviderPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = RVProviderPostAdapter.class.getName();
    private ArrayList<Post> posts;
    private Context mContext;

    private LinearLayoutManager linearLayoutManager;

    //type of item
    private final static int ITEM_LOADED = 0;
    private final static int ITEM_LOADING = 1;


    public RVProviderPostAdapter(final RecyclerView recyclerView, Context context) {
        posts = new ArrayList<>();
        linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        this.mContext = context;

    }

    //hold view cho moi item da load trong recycle view
    public class PostHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvTime, tvContent;
        ImageView imageView, imgAvatar;

        public PostHolder(View itemView) {
            super(itemView);

            tvUserName =itemView.findViewById(R.id.tvUserName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);

            imageView = itemView.findViewById(R.id.imageView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);

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
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostHolder(itemView);
        }
        return null;
    }

    //hien thi du lieu len item
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PostHolder) {
            PostHolder postHolder = (PostHolder) holder;
            postHolder.tvUserName.setText(Common.getInstance().getUserName());
            postHolder.imgAvatar.setImageBitmap(Common.getInstance().getBmpAvatar());



            postHolder.tvTime.setText(posts.get(position).getTillNow());
            postHolder.tvContent.setText(posts.get(position).getDescription());
            postHolder.imageView.setImageBitmap(posts.get(position).getImgBitmap());

            if (posts.get(position).getImageUrl().isEmpty()) {
                postHolder.imageView.setVisibility(View.GONE);
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
        return posts.size();
    }
    //interface dung de call back moi khi load them thong bao
    public interface ILoadMoreCallBack {
        void onLoadMore();
        void onLoadMoreFinish();
    }

    //tra ve trang thai cua thong bao, tu do xac dinh dung viewholder nao
    @Override
    public int getItemViewType(int position) {
        return posts.get(position) == null ? ITEM_LOADING : ITEM_LOADED;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void addPost(Post feed) {
        posts.add(feed);
    }

    public void insertPost(int position, Post feed) {
        posts.add(position, feed);
        this.notifyItemInserted(position);
    }
}
