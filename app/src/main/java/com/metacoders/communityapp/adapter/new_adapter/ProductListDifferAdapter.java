package com.metacoders.communityapp.adapter.new_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.ConvertTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProductListDifferAdapter extends RecyclerView.Adapter<ProductListDifferAdapter.DiffferViewholder> {
    //callBack
    private static final int BIG_ROW = 1;
    private static final int SMALL_ROW = 0;
    Context context;
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

    public ProductListDifferAdapter(Context context, ItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        // this.mData = productList;
        this.context = context;
        this.itemClickListener = itemClickListener;
        mDiffer = new AsyncListDiffer<>(this, diffCallback);

    }

    @Override
    public int getItemViewType(int position) {


        if (position == 0) {

            return BIG_ROW;

        } else {
            return SMALL_ROW;
        }


    }

    //method to submit list
    public void submitlist(List<Post.PostModel> data) {
        // mDiffer.submitList(null);
        mDiffer.submitList(new ArrayList<>(data));
        Log.d("TAG", "submitList: " + data.size());
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

        if (viewType == BIG_ROW) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_video_home, parent, false);
        } else if (viewType == SMALL_ROW) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_video_mini, parent, false);
        }

        return new DiffferViewholder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiffferViewholder holder, int position) {

        // holder.itemView.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.item_animation_fall_down));

        Post.PostModel newsFeed = mDiffer.getCurrentList().get(position);
        //viewHolder.itemView.animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_fall_down)

        holder.author.setText("");
        holder.title.setText(newsFeed.getTitle());
//        holder.price.setText("$" + newsFeed.getProduct_price().toString());
//        holder.description.setText(newsFeed.getDescription());
        holder.viewCount.setText(newsFeed.getHit() + "");
        holder.commentCount.setText("0");
        //  Log.d("TAGE", "onBindViewHolder: "+ newsFeed.getStatus() + " Vis" + newsFeed.getVisibility());
        // convert time
        SimpleDateFormat df = new SimpleDateFormat(Constants.CREATED_AT_FORMAT);
        try {
            Date date = df.parse(newsFeed.getCreated_at());
            holder.date.setText(ConvertTime.getTimeAgo(date));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.date.setText(newsFeed.getCreated_at());
        }
        String imageLink = "";

        if (newsFeed.getImage() == null || newsFeed.getImage().isEmpty()) {
            imageLink = newsFeed.getThumb() + "";
        } else imageLink = newsFeed.getImage() + "";

        Log.d("TAGGED", "onBindViewHolder: " + imageLink);
        Glide.with(context)
                .load(imageLink)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .thumbnail(/*sizeMultiplier=*/ 0.25f)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);


        // setting details
        try {
            holder.desc.setText(newsFeed.getDescription().toString() + "");
        } catch (Exception r) {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(newsFeed);
            }
        });

        if (newsFeed.getType().contains("video")) {
            holder.playBtn.setVisibility(View.VISIBLE);
        } else if (newsFeed.getType().contains("audio")) {
            holder.playBtn.setVisibility(View.VISIBLE);
        } else holder.playBtn.setVisibility(View.GONE);


    }


    public interface ItemClickListener {
        void onItemClick(Post.PostModel model);
    }

    public class DiffferViewholder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        public ImageView image, playBtn;
        public TextView author, viewCount, date;
        public TextView commentCount;
        public CardView container;
        ItemClickListener itemClickListener;

        public DiffferViewholder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            desc = itemView.findViewById(R.id.title_short_details);
            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.video_thumb);
            playBtn = itemView.findViewById(R.id.play_btn);
            author = itemView.findViewById(R.id.video_author);
            viewCount = itemView.findViewById(R.id.video_view);
            date = itemView.findViewById(R.id.video_date);
            container = itemView.findViewById(R.id.container);
            commentCount = itemView.findViewById(R.id.video_comment);


        }

    }

}

