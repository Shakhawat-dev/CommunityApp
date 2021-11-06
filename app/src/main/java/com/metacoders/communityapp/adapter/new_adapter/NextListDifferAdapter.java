package com.metacoders.communityapp.adapter.new_adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.newModels.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NextListDifferAdapter extends RecyclerView.Adapter<NextListDifferAdapter.DiffferViewholder> {
    Context context;
    private Boolean will_show_big_row = false;
    private AsyncListDiffer<Post.PostModel> mDiffer;
    private List<Post.PostModel> mData = new ArrayList<>();
    // private List<ProductModel> mDataFiltered = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;
    private DiffUtil.ItemCallback<Post.PostModel> diffCallback = new DiffUtil.ItemCallback<Post.PostModel>() {
        @Override
        public boolean areItemsTheSame(Post.PostModel oldItem, Post.PostModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Post.PostModel oldItem, Post.PostModel newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getSlug().equals(newItem.getSlug());
        }

    };

    public NextListDifferAdapter(Context context, ItemClickListener itemClickListener, Boolean will_show_big_row) {

        this.mInflater = LayoutInflater.from(context);
        // this.mData = productList;
        this.context = context;
        this.itemClickListener = itemClickListener;
        mDiffer = new AsyncListDiffer<>(this, diffCallback);
        this.will_show_big_row = will_show_big_row;

    }


    //method to submit list
    public void submitlist(List<Post.PostModel> data) {
        // mDiffer.submitList(null);
        mDiffer.submitList(new ArrayList<>(data));
       // Log.d("TAG", "submitList: " + data.size());
    }

    //method getItem by position
    public Post.PostModel getItem(int position) {
        return mDiffer.getCurrentList().get(position);
    }

    public List<Post.PostModel> getCurrentList() {
        return mDiffer.getCurrentList();
    }


    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    //override the method of Adapter

    @NonNull
    @Override
    public DiffferViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;


        view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row_video_mini_next_new, parent, false);


        return new DiffferViewholder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiffferViewholder holder, int position) {

        // holder.itemView.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.item_animation_fall_down));

        Post.PostModel newsFeed = mDiffer.getCurrentList().get(position);
        //viewHolder.itemView.animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_fall_down)

        holder.title.setText(newsFeed.getTitle());


        String imageLink = "";
        boolean isVideo = false;

        if (newsFeed.getType().equals("video")) {
            if ((newsFeed.getThumb() == null || newsFeed.getThumb().isEmpty())) {
                imageLink = newsFeed.getPath() + "";
                isVideo = true;
            } else {
                isVideo = false;
                imageLink = newsFeed.getThumb() + "";
            }

        } else {
            isVideo = false;
            if ((newsFeed.getThumb() == null || newsFeed.getThumb().isEmpty())) {
                imageLink = newsFeed.getImage() + "";
            } else imageLink = newsFeed.getThumb() + "";
        }

//
//
//

        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


        Glide.with(context)
                .load(imageLink)
                .centerCrop()
                .transition(withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(newsFeed);
            }
        });

//        if (newsFeed.getType().contains("video")) {
//            holder.playBtn.setVisibility(View.VISIBLE);
//        } else if (newsFeed.getType().contains("audio")) {
//            holder.playBtn.setVisibility(View.VISIBLE);
//        } else holder.playBtn.setVisibility(View.GONE);


    }

    public void loadImage(String url) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //give YourVideoUrl below
        retriever.setDataSource("YourVideoUrl", new HashMap<String, String>());
// this gets frame at 2nd second
        Bitmap image = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//use this bitmap image
    }

    public interface ItemClickListener {
        void onItemClick(Post.PostModel model);
    }

    public class DiffferViewholder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        ItemClickListener itemClickListener;

        public DiffferViewholder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.video_thumb);


        }

    }

}

