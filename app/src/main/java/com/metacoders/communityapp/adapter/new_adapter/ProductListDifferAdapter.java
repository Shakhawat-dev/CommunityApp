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
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.ConvertTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProductListDifferAdapter extends RecyclerView.Adapter<ProductListDifferAdapter.DiffferViewholder> {
    //callBack

    private static final int VIDEO_ROW = 1;
    private static final int AUDIO_ROW = 0;
    private static final int NEWS_ROW = 2;
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

    public ProductListDifferAdapter(Context context, ItemClickListener itemClickListener, Boolean will_show_big_row) {

        this.mInflater = LayoutInflater.from(context);
        // this.mData = productList;
        this.context = context;
        this.itemClickListener = itemClickListener;
        mDiffer = new AsyncListDiffer<>(this, diffCallback);
        this.will_show_big_row = will_show_big_row;

    }

    @Override
    public int getItemViewType(int position) {

        int viewType = 2;


        if (mDiffer.getCurrentList().size() > 0) {

            if (mDiffer.getCurrentList().get(position).getType().toString().contains("audio")) {
                return AUDIO_ROW;
            } else if (mDiffer.getCurrentList().get(position).getType().toString().contains("video")) {
                return VIDEO_ROW;
            } else {
                return NEWS_ROW;
            }
        } else {
            return 2;
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

        if (viewType == VIDEO_ROW) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.new_big_row_video, parent, false);
        } else if (viewType == AUDIO_ROW) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.new_big_row_audio, parent, false);
        } else {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.new_big_row_news, parent, false);
        }

        return new DiffferViewholder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiffferViewholder holder, int position) {

        // holder.itemView.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.item_animation_fall_down));

        Post.PostModel newsFeed = mDiffer.getCurrentList().get(position);
        //viewHolder.itemView.animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_fall_down)

        holder.author.setText(newsFeed.getName());
        holder.country_name.setText(newsFeed.getCountry());
        holder.title.setText(newsFeed.getTitle());
        holder.viewCount.setText(newsFeed.getHit() + "");
        holder.more_option.setOnClickListener(v -> {

        });

        //  Log.d("TAGE", "onBindViewHolder: "+ newsFeed.getStatus() + " Vis" + newsFeed.getVisibility());
        // convert time
        SimpleDateFormat df = new SimpleDateFormat(Constants.CREATED_AT_FORMAT);
        try {
            // Date date = df.parse(newsFeed.getCreated_at());

            holder.date.setText(ConvertTime.covertTimeToText(newsFeed.getCreated_at()));

        } catch (Exception e) {
            e.printStackTrace();
            holder.date.setText(newsFeed.getCreated_at());
        }
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


        // setting details
        try {
            holder.desc.setText(newsFeed.getDescription().toString() + "");
        } catch (Exception r) {
            holder.desc.setText("");
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
        public TextView title, desc;
        public ImageView image, playBtn, more_option;
        public TextView author, viewCount, date, country_name;
        //  public TextView commentCount;
        //  public CardView container;
        ItemClickListener itemClickListener;

        public DiffferViewholder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            more_option = itemView.findViewById(R.id.post_option);
            desc = itemView.findViewById(R.id.singleLineDesc);
            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.video_thumb);
            playBtn = itemView.findViewById(R.id.play_btn);
            author = itemView.findViewById(R.id.video_author);
            viewCount = itemView.findViewById(R.id.video_view);
            date = itemView.findViewById(R.id.video_date);
            country_name = itemView.findViewById(R.id.country_name);

//            container = itemView.findViewById(R.id.container);
            //   commentCount = itemView.findViewById(R.id.video_comment);


        }

    }

}

