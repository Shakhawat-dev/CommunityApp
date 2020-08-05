package com.metacoders.communityapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.utils.ConvertTime;
import com.metacoders.communityapp.utils.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    private Context ctx;
    private List<Post_Model> newsfeedList;
    private ItemClickListenter itemClickListenter;

    public NewsFeedAdapter(Context ctx, List<Post_Model> newsfeedList, ItemClickListenter itemClickListenter) {
        this.ctx = ctx;
        this.newsfeedList = newsfeedList;
        this.itemClickListenter = itemClickListenter;
    }

    @NonNull
    @Override
    public NewsFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_video_home, parent, false);

        return new ViewHolder(view, itemClickListenter);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedAdapter.ViewHolder holder, final int position) {

        Post_Model newsFeed = newsfeedList.get(position);

        holder.title.setText(newsFeed.getTitle());
//        holder.price.setText("$" + newsFeed.getProduct_price().toString());
//        holder.description.setText(newsFeed.getDescription());
        holder.viewCount.setText(newsFeed.getHit()+"");
        holder.commentCount.setText("0");
        // convert time
        SimpleDateFormat df = new SimpleDateFormat(Utils.CREATED_AT_FORMAT);
        try {
            Date date = df.parse(newsFeed.getCreatedAt());
            holder.date.setText(ConvertTime.getTimeAgo(date));
        } catch (ParseException e) {
            e.printStackTrace();
           holder.date.setText(newsFeed.getCreatedAt());
        }
        String link ;

        if(newsFeed.getImageMid().isEmpty() || newsFeed.getImageMid() == null)
        {
            link = newsFeed.getImageUrl() ;
        }
        else   link = Utils.IMAGE_URL + newsFeed.getImageMid() ;
        Glide.with(ctx)
                .load(link)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(/*sizeMultiplier=*/ 0.25f)
               // .placeholder(R.drawable.placeholder)
                .into(holder.image);

        if(newsFeed.getPostType().contains("video"))
        {
           // holder.price.setVisibility(View.VISIBLE);
        }
//        else  holder.price.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return newsfeedList.size();
    }

    public interface ItemClickListenter {
        void onItemClick(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public ImageView image , playBtn;
        public TextView author , viewCount , date ;
        public TextView commentCount;

        ItemClickListenter itemClickListenter;

        public ViewHolder(@NonNull View itemView, ItemClickListenter itemClickListenter) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.video_thumb);
            playBtn = itemView.findViewById(R.id.play_btn);
            author = itemView.findViewById(R.id.video_author);
            viewCount = itemView.findViewById(R.id.video_view) ;
            date = itemView.findViewById(R.id.video_date);
            commentCount = itemView.findViewById(R.id.video_comment);
            this.itemClickListenter = itemClickListenter;
        }

        @Override
        public void onClick(View v) {


           itemClickListenter.onItemClick( v, getAdapterPosition());

//            if (newsfeedList.get(position).getIs_product().equals("false")) {
//                Intent intent = new Intent(ctx, NewsDetailsActivity.class);
//                intent.putExtra("image", newsfeedList.get(position).getImage());
//                intent.putExtra("title", newsfeedList.get(position).getTitle());
//                intent.putExtra("desc" , newsfeedList.get(position).getDescription()) ;
//
//                ctx.startActivity(intent);
//            }
//            else {
//                Intent intent = new Intent(ctx, ProductDetailActivity.class);
//                intent.putExtra("isSingle" , true) ;
//                intent.putExtra("productID" ,newsfeedList.get(position).getProduct_id()) ;
//                ctx.startActivity(intent);
//            }


        }
    }
}
